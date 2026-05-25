package core.controllers;

import core.models.DataStore;
import core.models.Patient;
import core.models.Response;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.json.JSONObject;

/**
 * Controller for patient-related operations.
 * Handles registration and profile updates.
 *
 * Field validation is delegated to UserValidator (SRP).
 * Views must NOT contain validation logic — delegate to this controller.
 */
public class PatientController implements IPatientController {

    private final DataStore dataStore;

    public PatientController() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Registers a new patient with full validation.
     *
     * Validations (via UserValidator — SRP):
     *   - Required fields must not be empty
     *   - ID must be exactly 12 numeric digits
     *   - Phone must be exactly 10 numeric digits (if provided)
     *   - Email must match standard format (if provided)
     *   - Passwords must match
     *   - ID must be unique across all users
     *   - Username must be unique
     *   - Birthdate must be valid YYYY-MM-DD
     *
     * @return Response(CREATED) on success; Response(BAD_REQUEST/CONFLICT) on failure.
     */
    @Override
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

        // Delegate field-format validation to UserValidator (SRP)
        String idErr = UserValidator.validateId(idStr);
        if (idErr != null) return new Response(Response.BAD_REQUEST, idErr);

        String pwErr = UserValidator.validatePasswordMatch(password, confirmPassword);
        if (pwErr != null) return new Response(Response.BAD_REQUEST, pwErr);

        String phoneErr = UserValidator.validatePhone(phoneStr);
        if (phoneErr != null) return new Response(Response.BAD_REQUEST, phoneErr);

        String emailErr = UserValidator.validateEmail(email);
        if (emailErr != null) return new Response(Response.BAD_REQUEST, emailErr);

        String userErr = UserValidator.validateUsername(username);
        if (userErr != null) return new Response(Response.BAD_REQUEST, userErr);

        long id = Long.parseLong(idStr);
        long phone = 0L;
        if (!isEmpty(phoneStr)) {
            phone = Long.parseLong(phoneStr.trim());
        }

        // Uniqueness checks
        if (dataStore.userIdExists(id)) {
            return new Response(Response.CONFLICT,
                    "A user with ID " + id + " already exists.");
        }
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
                "Patient '" + firstname.trim() + " " + lastname.trim()
                + "' registered successfully. You can now log in.", data);
    }

    /**
     * Updates an existing patient's profile.
     * Only non-empty fields are updated (partial update).
     * Delegates format validation to UserValidator (SRP).
     *
     * @return Response(OK) on success; Response(BAD_REQUEST/NOT_FOUND) on failure.
     */
    @Override
    public Response updatePatient(long patientId, String firstname, String lastname,
                                   String password, String confirmPassword,
                                   String email, String birthdateStr, String genderStr,
                                   String phoneStr, String address) {
        Patient patient = dataStore.findPatientById(patientId);
        if (patient == null) {
            return new Response(Response.NOT_FOUND,
                    "Patient with ID " + patientId + " not found.");
        }

        // Password update
        if (!isEmpty(password)) {
            String pwErr = UserValidator.validatePasswordMatch(password, confirmPassword);
            if (pwErr != null) return new Response(Response.BAD_REQUEST, pwErr);
            patient.setPassword(password);
        }

        if (!isEmpty(firstname)) patient.setFirstname(firstname.trim());
        if (!isEmpty(lastname))  patient.setLastname(lastname.trim());
        if (!isEmpty(address))   patient.setAddress(address.trim());

        // Email validation
        if (!isEmpty(email)) {
            String emailErr = UserValidator.validateEmail(email);
            if (emailErr != null) return new Response(Response.BAD_REQUEST, emailErr);
            patient.setEmail(email.trim());
        }

        // Phone validation
        if (!isEmpty(phoneStr)) {
            String phoneErr = UserValidator.validatePhone(phoneStr);
            if (phoneErr != null) return new Response(Response.BAD_REQUEST, phoneErr);
            patient.setPhone(Long.parseLong(phoneStr.trim()));
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
