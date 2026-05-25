package core.controllers;

import core.models.Doctor;
import core.models.DataStore;
import core.models.Specialty;
import java.time.LocalDateTime;

/**
 * Strategy interface for resolving which doctor to assign to an appointment request.
 *
 * Implementations:
 *   - RequestByDoctor    — uses an explicit doctor ID provided by the patient
 *   - RequestBySpecialty — auto-assigns the first available doctor with the required specialty
 *
 * Satisfies the Open/Closed Principle: new assignment strategies can be added
 * without modifying AppointmentController.
 */
public interface AppointmentRequestStrategy {

    /**
     * Resolves the doctor to assign to the new appointment.
     *
     * @param doctorId  explicit doctor ID (may be ignored by specialty-based strategies)
     * @param specialty required specialty
     * @param datetime  requested appointment date/time (used for availability check)
     * @param dataStore data source
     * @return the resolved Doctor, or {@code null} if resolution fails
     */
    Doctor resolveDoctor(long doctorId, Specialty specialty,
                         LocalDateTime datetime, DataStore dataStore);

    /**
     * Returns a human-readable error message when {@link #resolveDoctor} returns null.
     */
    String getErrorMessage();
}
