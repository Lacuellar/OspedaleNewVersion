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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Controller for appointment-related operations.
 *
 * Business rules enforced:
 *   - Appointment time minutes must be in {00, 15, 30, 45}
 *   - Appointment must be scheduled in the future
 *   - Doctor conflict check: no overlap within 15-minute slots
 *   - Prescriptions can only be added to PENDING appointments
 *   - Only REQUESTED or PENDING appointments can be cancelled
 *   - Rescheduling respects all constraints of a new appointment
 *
 * Doctor assignment is delegated to AppointmentRequestStrategy (OCP):
 *   - doctorId > 0 → RequestByDoctor
 *   - doctorId ≤ 0 → RequestBySpecialty
 *
 * Field validation is delegated to AppointmentValidator (SRP).
 *
 * Views must NOT implement any of this logic — call the controller and display the Response.
 */
public class AppointmentController implements IAppointmentController {

    private final DataStore dataStore;

    /** Slot size in minutes (minimum gap between appointments). */
    private static final int SLOT_MINUTES = 15;

    public AppointmentController() {
        this.dataStore = DataStore.getInstance();
    }

    // =========================================================================
    // CREATE
    // =========================================================================

    /**
     * Creates a new appointment request.
     *
     * @param patientId  patient's numeric ID
     * @param doctorId   doctor's numeric ID; pass ≤ 0 to auto-assign by specialty
     * @param specialty  medical specialty
     * @param dateStr    date string  "YYYY-MM-DD"
     * @param timeStr    time string  "HH:mm"
     * @param reason     reason for visit
     * @param isInPerson true = in-person, false = virtual
     * @return Response(CREATED, ..., data{appointmentId, doctorName, datetime}) on success
     */
    @Override
    public Response createAppointment(long patientId, long doctorId, Specialty specialty,
                                       String dateStr, String timeStr,
                                       String reason, boolean isInPerson) {

        if (isEmpty(reason) || specialty == null) {
            return new Response(Response.BAD_REQUEST,
                    "Please fill all required fields (date, time, reason, specialty).");
        }

        Patient patient = dataStore.findPatientById(patientId);
        if (patient == null) {
            return new Response(Response.NOT_FOUND, "Patient not found.");
        }

        // Validate and parse date/time via AppointmentValidator (SRP)
        String[] errOut = {null};
        LocalDateTime datetime = AppointmentValidator.parseAndValidateDatetime(dateStr, timeStr, errOut);
        if (datetime == null) {
            return new Response(Response.BAD_REQUEST, errOut[0]);
        }

        String futureErr = AppointmentValidator.validateFutureDatetime(datetime);
        if (futureErr != null) return new Response(Response.BAD_REQUEST, futureErr);

        String slotErr = AppointmentValidator.validateSlotMinutes(datetime);
        if (slotErr != null) return new Response(Response.BAD_REQUEST, slotErr);

        // Select strategy based on whether a specific doctor was requested (OCP — Strategy pattern)
        AppointmentRequestStrategy strategy = (doctorId > 0)
                ? new RequestByDoctor()
                : new RequestBySpecialty();

        Doctor doctor = strategy.resolveDoctor(doctorId, specialty, datetime, dataStore);
        if (doctor == null) {
            return new Response(Response.NOT_FOUND, strategy.getErrorMessage());
        }

        String appointmentId = dataStore.generateAppointmentId(patientId);
        Appointment appointment = new Appointment(
                appointmentId, patient, doctor, specialty, datetime, reason.trim(), isInPerson);
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
    @Override
    public Response acceptAppointment(String appointmentId) {
        String idErr = AppointmentValidator.validateAppointmentId(appointmentId);
        if (idErr != null) return new Response(Response.BAD_REQUEST, idErr);

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
        dataStore.notifyObservers("appointments");
        return new Response(Response.OK,
                "Appointment " + appointmentId + " accepted and is now PENDING.");
    }

    /**
     * Completes an appointment: PENDING → COMPLETED.
     * Saves medical notes (diagnosis, observations, treatment, follow-up).
     */
    @Override
    public Response completeAppointment(String appointmentId, String diagnosis,
                                         String observations, String treatment, String followUp) {
        String idErr = AppointmentValidator.validateAppointmentId(appointmentId);
        if (idErr != null) return new Response(Response.BAD_REQUEST, idErr);

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
        dataStore.notifyObservers("appointments");
        return new Response(Response.OK,
                "Appointment " + appointmentId + " completed successfully.");
    }

    /**
     * Cancels an appointment.
     * Allowed from REQUESTED or PENDING status only.
     */
    @Override
    public Response cancelAppointment(String appointmentId) {
        String idErr = AppointmentValidator.validateAppointmentId(appointmentId);
        if (idErr != null) return new Response(Response.BAD_REQUEST, idErr);

        Appointment appt = dataStore.findAppointmentById(appointmentId);
        if (appt == null) {
            return new Response(Response.NOT_FOUND,
                    "Appointment '" + appointmentId + "' not found.");
        }
        if (appt.getStatus() == AppointmentStatus.COMPLETED) {
            return new Response(Response.CONFLICT, "Cannot cancel a COMPLETED appointment.");
        }
        if (appt.getStatus() == AppointmentStatus.CANCELED) {
            return new Response(Response.CONFLICT, "Appointment is already CANCELED.");
        }
        appt.setStatus(AppointmentStatus.CANCELED);
        dataStore.notifyObservers("appointments");
        return new Response(Response.OK, "Appointment " + appointmentId + " has been canceled.");
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
    @Override
    public Response rescheduleAppointment(String appointmentId,
                                           String newDateStr, String newTimeStr,
                                           String reason) {
        String idErr = AppointmentValidator.validateAppointmentId(appointmentId);
        if (idErr != null) return new Response(Response.BAD_REQUEST, idErr);

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

        String[] errOut = {null};
        LocalDateTime newDatetime = AppointmentValidator.parseAndValidateDatetime(newDateStr, newTimeStr, errOut);
        if (newDatetime == null) {
            return new Response(Response.BAD_REQUEST, errOut[0]);
        }

        String futureErr = AppointmentValidator.validateFutureDatetime(newDatetime);
        if (futureErr != null) return new Response(Response.BAD_REQUEST, futureErr);

        String slotErr = AppointmentValidator.validateSlotMinutes(newDatetime);
        if (slotErr != null) return new Response(Response.BAD_REQUEST, slotErr);

        // Doctor availability — exclude this appointment from the conflict check
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
                        + ". Please choose a time at least " + SLOT_MINUTES + " minutes apart.");
            }
        }

        appt.setDatetime(newDatetime);
        if (!isEmpty(reason)) {
            String updatedReason = appt.getReason()
                    + "\n[Rescheduled — reason: " + reason.trim() + "]";
            appt.setReason(updatedReason);
        }
        dataStore.notifyObservers("appointments");
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
    @Override
    public Response prescribe(String appointmentId,
                               String medicationName, String doseStr,
                               String route, String durationStr,
                               String instructions, String frequencyStr) {

        // Validate required prescription fields (SRP — AppointmentValidator)
        String fieldErr = AppointmentValidator.validatePrescriptionFields(
                medicationName, doseStr, route, durationStr, frequencyStr);
        if (fieldErr != null) return new Response(Response.BAD_REQUEST, fieldErr);

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
        dataStore.notifyObservers("appointments");

        JSONObject data = new JSONObject();
        data.put("appointmentId", appointmentId);
        data.put("medication", medicationName);
        data.put("prescriptionCount", appt.getPrescriptions().size());

        return new Response(Response.CREATED,
                "Prescription for '" + medicationName.trim() + "' added successfully. "
                + "Total prescriptions for this appointment: " + appt.getPrescriptions().size(), data);
    }

    // =========================================================================
    // QUERIES — serialized (returns JSON in Response, no model objects to views)
    // =========================================================================

    /**
     * Returns a single appointment as a serialized JSONObject.
     * The Response data contains key "appointment" → JSONObject with all fields.
     */
    @Override
    public Response getAppointmentResponse(String appointmentId) {
        Appointment appt = dataStore.findAppointmentById(appointmentId);
        if (appt == null) {
            return new Response(Response.NOT_FOUND,
                    "Appointment '" + appointmentId + "' not found.");
        }
        JSONObject obj = new JSONObject();
        obj.put("id",         appt.getId());
        obj.put("date",       appt.getDatetime() != null ? appt.getDatetime().toLocalDate().toString() : "");
        obj.put("time",       appt.getDatetime() != null ? appt.getDatetime().toLocalTime().toString() : "");
        obj.put("datetime",   appt.getDatetime() != null ? appt.getDatetime().toString() : "");
        obj.put("patientName", appt.getPatient() != null
                ? appt.getPatient().getFirstname() + " " + appt.getPatient().getLastname() : "");
        obj.put("doctorName",  appt.getDoctor() != null
                ? appt.getDoctor().getFirstname() + " " + appt.getDoctor().getLastname() : "");
        obj.put("specialty",   appt.getSpecialty() != null ? appt.getSpecialty().name() : "");
        obj.put("type",        appt.isType() ? "In-person" : "Remote");
        obj.put("status",      appt.getStatus() != null ? appt.getStatus().name() : "");
        obj.put("reason",      appt.getReason() != null ? appt.getReason() : "");

        JSONObject data = new JSONObject();
        data.put("appointment", obj);
        return new Response(Response.OK, "Appointment found.", data);
    }

    /**
     * Returns a patient's appointments as a serialized JSONArray, sorted descending by datetime.
     * The Response data contains key "appointments" → JSONArray.
     */
    @Override
    public Response getPatientAppointmentsResponse(long patientId) {
        Patient patient = dataStore.findPatientById(patientId);
        if (patient == null) {
            return new Response(Response.NOT_FOUND, "Patient not found.");
        }
        List<Appointment> list = new ArrayList<>(patient.getAppointments());
        list.sort(Comparator.comparing(Appointment::getDatetime).reversed());

        JSONArray arr = new JSONArray();
        for (Appointment a : list) {
            JSONObject obj = new JSONObject();
            obj.put("id",         a.getId());
            obj.put("datetime",   a.getDatetime() != null ? a.getDatetime().toString() : "");
            obj.put("doctorName", a.getDoctor() != null
                    ? a.getDoctor().getFirstname() + " " + a.getDoctor().getLastname() : "");
            obj.put("specialty",  a.getSpecialty() != null ? a.getSpecialty().name() : "");
            obj.put("type",       a.isType() ? "In-person" : "Remote");
            obj.put("status",     a.getStatus() != null ? a.getStatus().name() : "");
            arr.put(obj);
        }

        JSONObject data = new JSONObject();
        data.put("appointments", arr);
        return new Response(Response.OK,
                "Found " + arr.length() + " appointment(s) for patient " + patientId + ".", data);
    }

    /**
     * Returns a doctor's appointments as a serialized JSONArray, sorted descending by datetime.
     * If pendingOnly is true, only REQUESTED and PENDING appointments are included.
     * The Response data contains key "appointments" → JSONArray.
     */
    @Override
    public Response getDoctorAppointmentsResponse(long doctorId, boolean pendingOnly) {
        Doctor doctor = dataStore.findDoctorById(doctorId);
        if (doctor == null) {
            return new Response(Response.NOT_FOUND, "Doctor not found.");
        }
        List<Appointment> list = new ArrayList<>(doctor.getAppointments());
        list.sort(Comparator.comparing(Appointment::getDatetime).reversed());

        JSONArray arr = new JSONArray();
        for (Appointment a : list) {
            if (pendingOnly && a.getStatus() != AppointmentStatus.REQUESTED
                    && a.getStatus() != AppointmentStatus.PENDING) {
                continue;
            }
            JSONObject obj = new JSONObject();
            obj.put("id",          a.getId());
            obj.put("datetime",    a.getDatetime() != null ? a.getDatetime().toString() : "");
            obj.put("patientName", a.getPatient() != null
                    ? a.getPatient().getFirstname() + " " + a.getPatient().getLastname() : "");
            obj.put("specialty",   a.getSpecialty() != null ? a.getSpecialty().name() : "");
            obj.put("type",        a.isType() ? "In-person" : "Remote");
            obj.put("status",      a.getStatus() != null ? a.getStatus().name() : "");
            arr.put(obj);
        }

        JSONObject data = new JSONObject();
        data.put("appointments", arr);
        return new Response(Response.OK,
                "Found " + arr.length() + " appointment(s) for doctor " + doctorId + ".", data);
    }

    // =========================================================================
    // PRIVATE HELPERS
    // =========================================================================

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
