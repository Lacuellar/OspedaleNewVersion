package core.controllers;

import core.models.Appointment;
import core.models.AppointmentStatus;
import core.models.DataStore;
import core.models.Doctor;
import core.models.Specialty;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Strategy that assigns the doctor explicitly chosen by the patient.
 * Validates that the doctor exists and is available at the requested time slot.
 *
 * Part of the Strategy pattern (OCP): AppointmentController delegates doctor
 * resolution to this class without knowing the concrete lookup algorithm.
 */
public class RequestByDoctor implements AppointmentRequestStrategy {

    private static final int SLOT_MINUTES = 15;
    private String errorMessage = "";

    @Override
    public Doctor resolveDoctor(long doctorId, Specialty specialty,
                                LocalDateTime datetime, DataStore dataStore) {
        Doctor doctor = dataStore.findDoctorById(doctorId);
        if (doctor == null) {
            errorMessage = "Doctor with ID " + doctorId + " not found. Please select a valid doctor.";
            return null;
        }
        if (!isAvailable(doctor, datetime)) {
            errorMessage = "Dr. " + doctor.getFirstname() + " " + doctor.getLastname()
                    + " is not available at the requested time. "
                    + "Please choose a time at least " + SLOT_MINUTES + " minutes apart from existing appointments.";
            return null;
        }
        return doctor;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    // -------------------------------------------------------------------------

    private boolean isAvailable(Doctor doctor, LocalDateTime datetime) {
        for (Appointment existing : doctor.getAppointments()) {
            if (existing.getStatus() == AppointmentStatus.CANCELED
                    || existing.getStatus() == AppointmentStatus.COMPLETED) {
                continue;
            }
            long diffSeconds = Math.abs(
                    existing.getDatetime().toEpochSecond(ZoneOffset.UTC)
                    - datetime.toEpochSecond(ZoneOffset.UTC));
            if (diffSeconds < SLOT_MINUTES * 60L) {
                return false;
            }
        }
        return true;
    }
}
