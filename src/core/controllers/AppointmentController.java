package core.controllers;

import core.models.Appointment;
import core.models.AppointmentStatus;
import core.models.DataStore;
import core.models.Doctor;
import core.models.Patient;
import core.models.Prescription;
import core.models.Response;
import core.models.Specialty;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.json.JSONObject;

/**
 * Controller for appointment-related operations.
 *
 * Business rules enforced:
 *   - Appointment time minutes must be in {00, 15, 30, 45}
 *   - Appointment must be scheduled in the future
 *   - Doctor must not have a conflicting appointment within 15-minute slots
 *   - Prescriptions can only be added to PENDING appointments
 *   - Only REQUESTED or PENDING appointments can be cancelled
 *   - Rescheduling respects all constraints of a new appointment
 *
 * Views must NOT implement any of this logic — call the controller and display the Response.
 */
public class AppointmentController {

    private final DataStore dataStore;

    /** Slot size in minutes (doctor's minimum gap between appointments). */
    private static final int SLOT_MINUTES = 15;

    /** Allowed minute values for appointment times. */
    private static final int[] VALID_MINUTES = {0, 15, 30, 45};

    public AppointmentController() {
        this.dataStore = DataStore.getInstance();
    }

    // =========================================================================
    // CREATE
    // =========================================================================

    /**
     * Creates a new appointment.
     *
     * @param patientId  patient's numeric ID
     * @param doctorId   doctor's numeric ID (or -1 if scheduling by specialty)
     * @param specialty  medical specialty
     * @param dateStr    date string  "YYYY-MM-DD"
     * @param timeStr    time string  "HH:mm"
     * @param reason     reason for visit
     * @param isVirtual  true = virtual, false = in-person
     * @return Response(CREATED, ..., data{appointmentId, doctorName, datetime}) on success
     */
    public Response createAppointment(long patientId, long doctorId, Specialty specialty,
                                       String dateStr, String timeStr,
                                       String reason, boolean isVirtual) {

        if (isEmpty(dateStr) || isEmpty(timeStr) || isEmpty(reason) || specialty == null) {
            return new Response(Response.BAD_REQUEST,
                    "Please fill all required fields (date, time, reason, specialty).");
        }

        Patient patient = dataStore.findPatientById(patientId);
        if (patient == null) {
            return new Response(Response.NOT_FOUND, "Patient not found.");
        }

        Doctor doctor = dataStore.findDoctorById(doctorId);
        if (doctor == null) {
            return new Response(Response.NOT_FOUND, "Doctor not found. Please select a valid doctor.");
        }

        // Parse datetime
        LocalDateTime datetime;
        try {
            datetime = LocalDateTime.parse(dateStr.trim() + "T" + timeStr.trim());
        } catch (DateTimeParseException e) {
            return new Response(Response.BAD_REQUEST,
                    "Invalid date/time. Use YYYY-MM-DD for date and HH:mm for time.");
        }

        // Must be in the future
        if (!datetime.isAfter(LocalDateTime.now())) {
            return new Response(Response.BAD_REQUEST,
                    "Appointment must be scheduled for a future date and time.");
        }

        // Minutes must be in {0, 15, 30, 45}
        if (!isValidMinute(datetime.getMinute())) {
            return new Response(Response.BAD_REQUEST,
                    "Appointment time minutes must be 00, 15, 30, or 45.");
        }

        // Doctor availability — no overlap within SLOT_MINUTES
        for (Appointment existing : doctor.getAppointments()) {
            if (existing.getStatus() == AppointmentStatus.CANCELED
                    || existing.getStatus() == AppointmentStatus.COMPLETED) {
                continue;
            }
            long diffSeconds = Math.abs(
                    existing.getDatetime().toEpochSecond(ZoneOffset.UTC)
                    - datetime.toEpochSecond(ZoneOffset.UTC));
            if (diffSeconds < SLOT_MINUTES * 60L) {
                return new Response(Response.CONFLICT,
                        "Dr. " + doctor.getFirstname() + " " + doctor.getLastname()
                        + " already has an appointment at " + existing.getDatetime().toLocalTime()
                        + " on " + existing.getDatetime().toLocalDate()
                        + ". Please choose a time at least 15 minutes apart.");
            }
        }

        String appointmentId = dataStore.generateAppointmentId(patientId);
        Appointment appointment = new Appointment(
                appointmentId, patient, doctor, specialty, datetime, reason, isVirtual);
        dataStore.addAppointment(appointment);

        JSONObject data = new JSONObject();
        data.put("appointmentId", appointmentId);
        data.put("doctorName", doctor.getFirstname() + " " + doctor.getLastname());
        data.put("datetime", datetime.toString());

        return new Response(Response.CREATED,
                "Appointment " + appointmentId + " scheduled with Dr. "
                + doctor.getFirstname() + " " + doctor.getLastname()
                + " on " + dateStr + " at " + timeStr + ".", data);
    }

    // =========================================================================
    // STATUS TRANSITIONS
    // =========================================================================

    /**
     * Accepts (confirms) an appointment: REQUESTED → PENDING.
     */
    public Response acceptAppointment(String appointmentId) {
        Appointment appt = dataStore.findAppointmentById(appointmentId);
        if (appt == null) {
            return new Response(Response.NOT_FOUND,
                    "Appointment '" + appointmentId + "' not found.");
        }
        if (appt.getStatus() != AppointmentStatus.REQUESTED) {
            return new Response(Response.CONFLICT,
                    "Appointment must be in REQUESTED status to accept. Current status: " + appt.getStatus());
        }
        appt.setStatus(AppointmentStatus.PENDING);
        return new Response(Response.OK,
                "Appointment " + appointmentId + " accepted and is now PENDING.");
    }

    /**
     * Completes an appointment: PENDING → COMPLETED.
     * Saves medical notes (diagnosis, observations, treatment, follow-up).
     */
    public Response completeAppointment(String appointmentId, String diagnosis,
                                         String observations, String treatment, String followUp) {
        Appointment appt = dataStore.findAppointmentById(appointmentId);
        if (appt == null) {
            return new Response(Response.NOT_FOUND,
                    "Appointment '" + appointmentId + "' not found.");
        }
        if (appt.getStatus() != AppointmentStatus.PENDING) {
            return new Response(Response.CONFLICT,
                    "Appointment must be PENDING to complete. Current status: " + appt.getStatus());
        }
        appt.setStatus(AppointmentStatus.COMPLETED);
        appt.setDiagnosis(isEmpty(diagnosis) ? "" : diagnosis.trim());
        appt.setObservations(isEmpty(observations) ? "" : observations.trim());
        appt.setRecommendedTreatment(isEmpty(treatment) ? "" : treatment.trim());
        appt.setFollowUp(isEmpty(followUp) ? "" : followUp.trim());

        return new Response(Response.OK,
                "Appointment " + appointmentId + " completed successfully.");
    }

    /**
     * Cancels an appointment.
     * Allowed from REQUESTED or PENDING status only.
     */
    public Response cancelAppointment(String appointmentId) {
        Appointment appt = dataStore.findAppointmentById(appointmentId);
        if (appt == null) {
            return new Response(Response.NOT_FOUND,
                    "Appointment '" + appointmentId + "' not found.");
        }
        if (appt.getStatus() == AppointmentStatus.COMPLETED) {
            return new Response(Response.CONFLICT,
                    "Cannot cancel a COMPLETED appointment.");
        }
        if (appt.getStatus() == AppointmentStatus.CANCELED) {
            return new Response(Response.CONFLICT,
                    "Appointment is already CANCELED.");
        }
        appt.setStatus(AppointmentStatus.CANCELED);
        return new Response(Response.OK,
                "Appointment " + appointmentId + " has been canceled.");
    }

    // =========================================================================
    // RESCHEDULE
    // =========================================================================

    /**
     * Reschedules an appointment to a new date/time.
     * Applies all creation validations (future, minutes, doctor availability)
     * while excluding the current appointment from the conflict check.
     *
     * @param reason optional reason for rescheduling; appended to appointment notes.
     */
    public Response rescheduleAppointment(String appointmentId,
                                           String newDateStr, String newTimeStr,
                                           String reason) {
        Appointment appt = dataStore.findAppointmentById(appointmentId);
        if (appt == null) {
            return new Response(Response.NOT_FOUND,
                    "Appointment '" + appointmentId + "' not found.");
        }
        if (appt.getStatus() == AppointmentStatus.COMPLETED
                || appt.getStatus() == AppointmentStatus.CANCELED) {
            return new Response(Response.CONFLICT,
                    "Cannot reschedule a " + appt.getStatus() + " appointment.");
        }

        if (isEmpty(newDateStr) || isEmpty(newTimeStr)) {
            return new Response(Response.BAD_REQUEST,
                    "Please provide a new date (YYYY-MM-DD) and time (HH:mm).");
        }

        LocalDateTime newDatetime;
        try {
            newDatetime = LocalDateTime.parse(newDateStr.trim() + "T" + newTimeStr.trim());
        } catch (DateTimeParseException e) {
            return new Response(Response.BAD_REQUEST,
                    "Invalid date/time. Use YYYY-MM-DD for date and HH:mm for time.");
        }

        if (!newDatetime.isAfter(LocalDateTime.now())) {
            return new Response(Response.BAD_REQUEST,
                    "New appointment time must be in the future.");
        }

        if (!isValidMinute(newDatetime.getMinute())) {
            return new Response(Response.BAD_REQUEST,
                    "Appointment time minutes must be 00, 15, 30, or 45.");
        }

        // Doctor availability — exclude this appointment
        Doctor doctor = appt.getDoctor();
        for (Appointment existing : doctor.getAppointments()) {
            if (existing.getId().equals(appointmentId)) continue; // skip self
            if (existing.getStatus() == AppointmentStatus.CANCELED
                    || existing.getStatus() == AppointmentStatus.COMPLETED) continue;
            long diffSeconds = Math.abs(
                    existing.getDatetime().toEpochSecond(ZoneOffset.UTC)
                    - newDatetime.toEpochSecond(ZoneOffset.UTC));
            if (diffSeconds < SLOT_MINUTES * 60L) {
                return new Response(Response.CONFLICT,
                        "Doctor already has an appointment at " + existing.getDatetime().toLocalTime()
                        + " on " + existing.getDatetime().toLocalDate()
                        + ". Please choose a time at least 15 minutes apart.");
            }
        }

        // Apply changes
        appt.setDatetime(newDatetime);
        if (!isEmpty(reason)) {
            String updatedReason = appt.getReason()
                    + "\n[Rescheduled — reason: " + reason.trim() + "]";
            appt.setReason(updatedReason);
        }

        return new Response(Response.OK,
                "Appointment " + appointmentId + " rescheduled to "
                + newDateStr + " at " + newTimeStr + ".");
    }

    // =========================================================================
    // PRESCRIPTIONS
    // =========================================================================

    /**
     * Adds a prescription to a PENDING appointment.
     * Prescriptions are NOT allowed on REQUESTED, COMPLETED, or CANCELED appointments.
     *
     * @return Response(CREATED, ..., data{appointmentId, medication, prescriptionCount}) on success.
     */
    public Response prescribe(String appointmentId,
                               String medicationName, String doseStr,
                               String route, String durationStr,
                               String instructions, String frequencyStr) {

        if (isEmpty(medicationName) || isEmpty(doseStr) || isEmpty(route)
                || isEmpty(durationStr) || isEmpty(frequencyStr)) {
            return new Response(Response.BAD_REQUEST,
                    "Please fill all prescription fields (medication, dose, route, duration, frequency).");
        }

        Appointment appt = dataStore.findAppointmentById(appointmentId);
        if (appt == null) {
            return new Response(Response.NOT_FOUND,
                    "Appointment '" + appointmentId + "' not found.");
        }
        if (appt.getStatus() != AppointmentStatus.PENDING) {
            return new Response(Response.CONFLICT,
                    "Prescriptions can only be added to PENDING appointments. "
                    + "Current status: " + appt.getStatus()
                    + ".\nPlease accept the appointment first.");
        }

        double dose;
        int duration, frequency;
        try {
            dose = Double.parseDouble(doseStr.trim());
        } catch (NumberFormatException e) {
            return new Response(Response.BAD_REQUEST, "Dose must be a valid number (e.g., 500 or 0.5).");
        }
        try {
            duration = Integer.parseInt(durationStr.trim());
        } catch (NumberFormatException e) {
            return new Response(Response.BAD_REQUEST, "Duration must be a whole number (days).");
        }
        try {
            frequency = Integer.parseInt(frequencyStr.trim());
        } catch (NumberFormatException e) {
            return new Response(Response.BAD_REQUEST, "Frequency must be a whole number (times per day).");
        }

        Prescription prescription = new Prescription(
                appt, medicationName.trim(), dose, route.trim(),
                duration, isEmpty(instructions) ? "" : instructions.trim(), frequency);
        appt.addPrescription(prescription);

        JSONObject data = new JSONObject();
        data.put("appointmentId", appointmentId);
        data.put("medication", medicationName);
        data.put("prescriptionCount", appt.getPrescriptions().size());

        return new Response(Response.CREATED,
                "Prescription for '" + medicationName.trim() + "' added successfully. "
                + "Total prescriptions for this appointment: " + appt.getPrescriptions().size(), data);
    }

    // =========================================================================
    // QUERIES
    // =========================================================================

    /**
     * Returns a patient's appointments sorted descending by datetime (most recent first).
     */
    public List<Appointment> getPatientAppointmentsSorted(long patientId) {
        Patient patient = dataStore.findPatientById(patientId);
        if (patient == null) return new ArrayList<>();
        List<Appointment> list = new ArrayList<>(patient.getAppointments());
        list.sort(Comparator.comparing(Appointment::getDatetime).reversed());
        return list;
    }

    /**
     * Returns a doctor's appointments sorted descending by datetime (most recent first).
     */
    public List<Appointment> getDoctorAppointmentsSorted(long doctorId) {
        Doctor doctor = dataStore.findDoctorById(doctorId);
        if (doctor == null) return new ArrayList<>();
        List<Appointment> list = new ArrayList<>(doctor.getAppointments());
        list.sort(Comparator.comparing(Appointment::getDatetime).reversed());
        return list;
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private boolean isValidMinute(int minute) {
        for (int v : VALID_MINUTES) {
            if (minute == v) return true;
        }
        return false;
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
