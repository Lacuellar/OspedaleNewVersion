package core.models.dto;

/**
 * DTO representing the authenticated user's session data.
 * Returned by LoginController after a successful login.
 * Role is one of: "admin", "patient", "doctor".
 */
public record UserSessionDTO(
        long userId,
        String username,
        String fullName,
        String role
) {}
