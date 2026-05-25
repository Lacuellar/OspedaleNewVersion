package core.controllers;

import core.models.Appointment;
import core.models.AppointmentStatus;
import core.models.DataStore;
import core.models.Doctor;
import core.models.Specialty;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Strategy that automatically assigns the first available doctor matching
 * the required specialty when the patient has no preference.
 *
 * Part of the Strategy pattern (OCP): AppointmentController delegates doctor
 * resolution to this class without knowing the concrete search algorithm.
 */
public class RequestBySpecialty implements AppointmentRequestStrategy {

    private static final int SLOT_MINUTES = 15;
    private String errorMessage = "";

    @Override
    public Doctor resolveDoctor(long doctorId, Specialty specialty,
                                LocalDateTime datetime, DataStore dataStore) {
        for (Doctor d : dataStore.getDoctors()) {
            if (d.getSpecialty() == specialty && isAvailable(d, datetime)) {
                return d;
            }
        }
        errorMessage = "No available doctor found for specialty "
                + specialty.name().replace("_", " ")
                + " at the requested date and time. Please try a different time.";
        return null;
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
