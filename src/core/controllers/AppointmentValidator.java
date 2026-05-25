package core.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Stateless validator for appointment-related field rules.
 *
 * Each method returns {@code null} if the input is valid,
 * or an error message string if it is invalid.
 *
 * Satisfies SRP: appointment validation lives here, not in AppointmentController or views.
 */
public final class AppointmentValidator {

    /** Allowed minute values for appointment slots. */
    private static final int[] VALID_MINUTES = {0, 15, 30, 45};

    private AppointmentValidator() {}

    /**
     * Validates and parses "YYYY-MM-DD" + "HH:mm" into a LocalDateTime.
     * Returns {@code null} on success; sets error via the result array [0].
     */
    public static LocalDateTime parseAndValidateDatetime(String dateStr, String timeStr,
                                                          String[] errorOut) {
        if (dateStr == null || dateStr.trim().isEmpty()
                || timeStr == null || timeStr.trim().isEmpty()) {
            errorOut[0] = "Please provide a valid date (YYYY-MM-DD) and time (HH:mm).";
            return null;
        }
        LocalDateTime dt;
        try {
            dt = LocalDateTime.parse(dateStr.trim() + "T" + timeStr.trim());
        } catch (DateTimeParseException e) {
            errorOut[0] = "Invalid date/time. Use YYYY-MM-DD for date and HH:mm for time.";
            return null;
        }
        return dt;
    }

    /**
     * Appointment must be scheduled in the future.
     */
    public static String validateFutureDatetime(LocalDateTime datetime) {
        if (!datetime.isAfter(LocalDateTime.now())) {
            return "Appointment must be scheduled for a future date and time.";
        }
        return null;
    }

    /**
     * Appointment time minutes must be one of {00, 15, 30, 45} (15-minute slots).
     */
    public static String validateSlotMinutes(LocalDateTime datetime) {
        int minute = datetime.getMinute();
        for (int v : VALID_MINUTES) {
            if (minute == v) return null;
        }
        return "Appointment time minutes must be 00, 15, 30, or 45 (15-minute slots).";
    }

    /**
     * Validates appointment ID is not null/empty.
     */
    public static String validateAppointmentId(String appointmentId) {
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            return "Appointment ID cannot be empty.";
        }
        return null;
    }

    /**
     * Validates all required prescription fields.
     */
    public static String validatePrescriptionFields(String medicationName, String doseStr,
                                                     String route, String durationStr,
                                                     String frequencyStr) {
        if (isEmpty(medicationName) || isEmpty(doseStr) || isEmpty(route)
                || isEmpty(durationStr) || isEmpty(frequencyStr)) {
            return "Please fill all prescription fields (medication, dose, route, duration, frequency).";
        }
        return null;
    }

    // -------------------------------------------------------------------------

    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
