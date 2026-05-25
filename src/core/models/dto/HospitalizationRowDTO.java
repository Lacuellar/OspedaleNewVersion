package core.models.dto;

import core.models.RoomType;

/**
 * Read-only DTO representing one row in a hospitalizations table.
 * Controllers populate this; views display it — no model objects cross the boundary.
 */
public record HospitalizationRowDTO(
        String id,
        String patientName,
        String doctorName,
        String date,
        String reason,
        RoomType roomType,
        String status
) {}
