package core.models.dto;

import core.models.Specialty;

/**
 * DTO carrying the raw form fields for doctor registration/update.
 * Passed from the view to the controller — views never build model objects directly.
 */
public record DoctorFormDTO(
        String idStr,
        String username,
        String firstname,
        String lastname,
        String password,
        String confirmPassword,
        Specialty specialty,
        String licenceNumber,
        String assignedOffice
) {}
