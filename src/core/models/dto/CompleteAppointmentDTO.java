package core.models.dto;

/**
 * DTO for completing an appointment with medical notes.
 * Passed from view → controller when the doctor closes/completes a consultation.
 */
public record CompleteAppointmentDTO(
        String appointmentId,
        String diagnosis,
        String observations,
        String treatment,
        String followUp
) {}
