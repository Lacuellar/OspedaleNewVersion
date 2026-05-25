package core.models.dto;

/**
 * Read-only DTO representing one row in an appointments table or combo box.
 * Controllers populate this; views display it — no model objects cross the boundary.
 */
public record AppointmentRowDTO(
        String id,
        String datetime,
        String doctorName,
        String patientName,
        String specialty,
        String type,
        String status
) {}
