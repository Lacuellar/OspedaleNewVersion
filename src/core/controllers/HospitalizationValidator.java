package core.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Stateless validator for hospitalization-related field rules.
 *
 * Each method returns {@code null} if the input is valid,
 * or an error message string if it is invalid.
 *
 * Satisfies SRP: hospitalization validation lives here, not in HospitalizationController or views.
 */
public final class HospitalizationValidator {

    private HospitalizationValidator() {}

    /**
     * Validates and parses a "YYYY-MM-DD" admission date string.
     * Returns the parsed date on success; sets error via the result array [0].
     */
    public static LocalDate parseAndValidateDate(String dateStr, String[] errorOut) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            errorOut[0] = "Admission date is required.";
            return null;
        }
        try {
            return LocalDate.parse(dateStr.trim());
        } catch (DateTimeParseException e) {
            errorOut[0] = "Invalid date format. Use YYYY-MM-DD (e.g., 2026-06-01).";
            return null;
        }
    }

    /**
     * Validates that reason and roomType are not null/empty.
     */
    public static String validateRequiredFields(String reason, Object roomType) {
        if (reason == null || reason.trim().isEmpty()) {
            return "Reason for hospitalization is required.";
        }
        if (roomType == null) {
            return "Please select a room type.";
        }
        return null;
    }

    /**
     * Validates a hospitalization ID is not null/empty.
     */
    public static String validateHospitalizationId(String hospId) {
        if (hospId == null || hospId.trim().isEmpty()) {
            return "Hospitalization ID cannot be empty.";
        }
        return null;
    }
}
