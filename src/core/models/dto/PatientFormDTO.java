package core.models.dto;

/**
 * DTO (Data Transfer Object) carrying the raw form fields for patient registration/update.
 * Passed from the view to the controller — views never build model objects directly.
 */
public record PatientFormDTO(
        String idStr,
        String username,
        String firstname,
        String lastname,
        String password,
        String confirmPassword,
        String email,
        String birthdateStr,
        String genderStr,
        String phoneStr,
        String address
) {}
