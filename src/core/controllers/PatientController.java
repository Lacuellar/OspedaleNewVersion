package core.controllers;

import core.models.DataStore;
import core.models.Patient;
import core.models.Response;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.json.JSONObject;

/**
 * Controller for patient-related operations.
 * Handles registration, login lookup, and profile updates.
 * All business logic and validations live here — views only display results.
 */
public class PatientController {

    private final DataStore dataStore;

    public PatientController() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Registers a new patient with full validation.
     *
     * Validations:
     *   - Required fields must not be empty
     *   - ID must be exactly 12 numeric digits
     *   - Phone must be exactly 10 numeric digits (if provided)
     *   - Email must match standard format (if provided)
     *   - Passwords must match
     *   - ID must be unique across all users
     *   - Username must be unique across all users
     *   - Birthdate must be valid YYYY-MM-DD
     *
     * @return Response(CREATED) on success; Response(BAD_REQUEST/CONFLICT) on failure.
     */
    public Response registerPatient(String idStr, String username, String firstname, String lastname,
                                    String password, String confirmPassword,
                                    String email, String birthdateStr, String genderStr,
                                    String phoneStr, String address) {

        // Required fields
        if (isEmpty(firstname) || isEmpty(lastname) || isEmpty(idStr)
                || isEmpty(birthdateStr) || isEmpty(username) || isEmpty(password)) {
            return new Response(Response.BAD_REQUEST,
                    "Please fill all required fields (firstname, lastname, ID, birthdate, username, password).");
        }

        // ID: exactly 12 digits
        if (!idStr.matches("\\d{12}")) {
            return new Response(Response.BAD_REQUEST,
                    "ID must be exactly 12 numeric digits (e.g., 123456789012).");
        }

        // Phone: exactly 10 digits (optional)
        long phone = 0L;
        if (!isEmpty(phoneStr)) {
            if (!phoneStr.matches("\\d{10}")) {
                return new Response(Response.BAD_REQUEST,
                        "Phone must be exactly 10 numeric digits (e.g., 3001234567).");
            }
            phone = Long.parseLong(phoneStr);
        }

        // Email format (optional)
        if (!isEmpty(email)) {
            if (!email.matches("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}")) {
                return new Response(Response.BAD_REQUEST,
                        "Invalid email format. Use: example@domain.com");
            }
        }

        // Password match
        if (!password.equals(confirmPassword)) {
            return new Response(Response.BAD_REQUEST, "Passwords do not match.");
        }

        long id = Long.parseLong(idStr);

        // Unique ID
        if (dataStore.userIdExists(id)) {
            return new Response(Response.CONFLICT,
                    "A user with ID " + id + " already exists.");
        }

        // Unique username
        if (dataStore.usernameExists(username.trim())) {
            return new Response(Response.CONFLICT,
                    "Username '" + username + "' is already taken.");
        }

        // Birthdate
        LocalDate birthdate;
        try {
            birthdate = LocalDate.parse(birthdateStr.trim());
        } catch (DateTimeParseException e) {
            return new Response(Response.BAD_REQUEST,
                    "Invalid birthdate format. Use YYYY-MM-DD (e.g., 1990-05-20).");
        }

        // Gender: "Male" → true, anything else → false
        boolean gender = "Male".equalsIgnoreCase(genderStr);

        Patient newPatient = new Patient(id, username.trim(), firstname.trim(), lastname.trim(),
                password, isEmpty(email) ? "" : email.trim(),
                birthdate, gender, phone, isEmpty(address) ? "" : address.trim());
        dataStore.addPatient(newPatient);

        JSONObject data = new JSONObject();
        data.put("patientId", id);
        data.put("username", username);
        return new Response(Response.CREATED,
                "Patient '" + firstname.trim() + " " + lastname.trim() + "' registered successfully. You can now log in.", data);
    }

    /**
     * Updates an existing patient's profile.
     * Only non-empty fields are updated (partial update).
     *
     * @return Response(OK) on success; Response(BAD_REQUEST/NOT_FOUND) on failure.
     */
    public Response updatePatient(long patientId, String firstname, String lastname,
                                   String password, String confirmPassword,
                                   String email, String birthdateStr, String genderStr,
                                   String phoneStr, String address) {
        Patient patient = dataStore.findPatientById(patientId);
        if (patient == null) {
            return new Response(Response.NOT_FOUND,
                    "Patient with ID " + patientId + " not found.");
        }

        // Password update (both fields must be filled)
        if (!isEmpty(password)) {
            if (!password.equals(confirmPassword)) {
                return new Response(Response.BAD_REQUEST, "Passwords do not match.");
            }
            patient.setPassword(password);
        }

        if (!isEmpty(firstname))  patient.setFirstname(firstname.trim());
        if (!isEmpty(lastname))   patient.setLastname(lastname.trim());
        if (!isEmpty(address))    patient.setAddress(address.trim());

        // Email validation
        if (!isEmpty(email)) {
            if (!email.matches("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}")) {
                return new Response(Response.BAD_REQUEST, "Invalid email format.");
            }
            patient.setEmail(email.trim());
        }

        // Phone validation
        if (!isEmpty(phoneStr)) {
            if (!phoneStr.matches("\\d{10}")) {
                return new Response(Response.BAD_REQUEST,
                        "Phone must be exactly 10 numeric digits.");
            }
            patient.setPhone(Long.parseLong(phoneStr));
        }

        // Birthdate
        if (!isEmpty(birthdateStr)) {
            try {
                patient.setBirthdate(LocalDate.parse(birthdateStr.trim()));
            } catch (DateTimeParseException e) {
                return new Response(Response.BAD_REQUEST,
                        "Invalid birthdate format. Use YYYY-MM-DD.");
            }
        }

        // Gender
        if (!isEmpty(genderStr) && !"Select one".equalsIgnoreCase(genderStr)) {
            patient.setGender("Male".equalsIgnoreCase(genderStr));
        }

        return new Response(Response.OK, "Patient information updated successfully.");
    }

    // -------------------------------------------------------------------------
    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
