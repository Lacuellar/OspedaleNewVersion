package core.models.dto;

import core.models.Specialty;

/**
 * DTO for a new appointment request.
 * doctorId <= 0 means "assign by specialty automatically".
 */
public record AppointmentRequestDTO(
        long patientId,
        long doctorId,
        Specialty specialty,
        String dateStr,
        String timeStr,
        String reason,
        boolean isInPerson
) {}
