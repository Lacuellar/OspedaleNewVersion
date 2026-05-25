package core.controllers;

/**
 * Stateless validator for user-related field rules.
 *
 * Each method returns {@code null} if the input is valid,
 * or an error message string if it is invalid.
 *
 * Satisfies SRP: validation logic lives here, not scattered across controllers or views.
 * Controllers call these helpers and wrap the returned message in a Response.
 */
public final class UserValidator {

    private UserValidator() {}

    /**
     * ID must be exactly 12 numeric digits.
     */
    public static String validateId(String idStr) {
        if (idStr == null || !idStr.matches("\\d{12}")) {
            return "ID must be exactly 12 numeric digits (e.g., 123456789012).";
        }
        return null;
    }

    /**
     * Passwords must not be empty and must match.
     */
    public static String validatePasswordMatch(String password, String confirmPassword) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty.";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }
        return null;
    }

    /**
     * Phone must be exactly 10 numeric digits (optional field — pass null or empty to skip).
     */
    public static String validatePhone(String phoneStr) {
        if (phoneStr == null || phoneStr.trim().isEmpty()) {
            return null; // optional
        }
        if (!phoneStr.matches("\\d{10}")) {
            return "Phone must be exactly 10 numeric digits (e.g., 3001234567).";
        }
        return null;
    }

    /**
     * Email must match a standard format (optional field — pass null or empty to skip).
     */
    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null; // optional
        }
        if (!email.matches("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}")) {
            return "Invalid email format. Use: example@domain.com";
        }
        return null;
    }

    /**
     * Doctor licence number must match: L-XXXXXXXXXX MTL (L- + 10 digits + space + MTL).
     */
    public static String validateLicenceNumber(String licenceNumber) {
        if (licenceNumber == null || !licenceNumber.matches("L-\\d{10} MTL")) {
            return "Licence number must follow format: L-XXXXXXXXXX MTL (e.g., L-1234567890 MTL).";
        }
        return null;
    }

    /**
     * Doctor office must match: O-XXX (O- + exactly 3 digits).
     */
    public static String validateOffice(String office) {
        if (office == null || !office.matches("O-\\d{3}")) {
            return "Office must follow format: O-XXX (e.g., O-101, O-204).";
        }
        return null;
    }

    /**
     * Username must not be null or empty.
     */
    public static String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "Username cannot be empty.";
        }
        return null;
    }
}
