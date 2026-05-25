package core.models.dto;

/**
 * DTO for adding a prescription to an appointment.
 * Passed from view → controller; controller creates the Prescription model object.
 */
public record PrescriptionDTO(
        String appointmentId,
        String medicationName,
        double dose,
        String route,
        int duration,
        String instructions,
        int frequency
) {}
