package core.controllers;

import core.models.DataStore;
import core.models.Doctor;
import core.models.Response;
import core.models.Specialty;
import org.json.JSONObject;

/**
 * Controller for doctor-related operations.
 * Handles registration and profile updates.
 *
 * Field validation is delegated to UserValidator (SRP).
 * Views must NOT contain validation logic — delegate to this controller.
 */
public class DoctorController implements IDoctorController {

    private final DataStore dataStore;

    public DoctorController() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Registers a new doctor with full validation.
     *
     * Validations (via UserValidator — SRP):
     *   - Required fields must not be empty
     *   - ID must be exactly 12 numeric digits
     *   - Passwords must match
     *   - Licence number must match: L-XXXXXXXXXX MTL  (L- + 10 digits + space + MTL)
     *   - Office must match: O-XXX  (O- + exactly 3 digits)
     *   - ID and username must be unique
     *
     * @return Response(CREATED) on success; Response(BAD_REQUEST/CONFLICT) on failure.
     */
    @Override
    public Response registerDoctor(String idStr, String username, String firstname, String lastname,
                                    String password, String confirmPassword, Specialty specialty,
                                    String licenceNumber, String assignedOffice) {

        if (isEmpty(firstname) || isEmpty(lastname) || isEmpty(idStr)
                || isEmpty(username) || isEmpty(password)
                || specialty == null || isEmpty(licenceNumber) || isEmpty(assignedOffice)) {
            return new Response(Response.BAD_REQUEST,
                    "Please fill all required fields.");
        }

        // Delegate field-format validation to UserValidator (SRP)
        String idErr = UserValidator.validateId(idStr);
        if (idErr != null) return new Response(Response.BAD_REQUEST, idErr);

        String pwErr = UserValidator.validatePasswordMatch(password, confirmPassword);
        if (pwErr != null) return new Response(Response.BAD_REQUEST, pwErr);

        String licenceErr = UserValidator.validateLicenceNumber(licenceNumber);
        if (licenceErr != null) return new Response(Response.BAD_REQUEST, licenceErr);

        String officeErr = UserValidator.validateOffice(assignedOffice);
        if (officeErr != null) return new Response(Response.BAD_REQUEST, officeErr);

        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return new Response(Response.BAD_REQUEST, "ID must be a numeric value.");
        }

        if (dataStore.userIdExists(id)) {
            return new Response(Response.CONFLICT,
                    "A user with ID " + id + " already exists.");
        }
        if (dataStore.usernameExists(username.trim())) {
            return new Response(Response.CONFLICT,
                    "Username '" + username + "' is already taken.");
        }

        Doctor doctor = new Doctor(id, username.trim(), firstname.trim(), lastname.trim(),
                password, specialty, licenceNumber.trim(), assignedOffice.trim());
        dataStore.addDoctor(doctor);

        JSONObject data = new JSONObject();
        data.put("doctorId", id);
        data.put("username", username);
        return new Response(Response.CREATED,
                "Dr. " + firstname.trim() + " " + lastname.trim() + " registered successfully.", data);
    }

    /**
     * Updates an existing doctor's profile.
     * Only non-empty / non-null fields are applied (partial update).
     * Delegates format validation to UserValidator (SRP).
     *
     * @return Response(OK) on success; Response(BAD_REQUEST/NOT_FOUND) on failure.
     */
    @Override
    public Response updateDoctor(long doctorId, String firstname, String lastname,
                                  String password, String confirmPassword,
                                  Specialty specialty, String licenceNumber, String assignedOffice) {

        Doctor doctor = dataStore.findDoctorById(doctorId);
        if (doctor == null) {
            return new Response(Response.NOT_FOUND,
                    "Doctor with ID " + doctorId + " not found.");
        }

        // Password update
        if (!isEmpty(password)) {
            String pwErr = UserValidator.validatePasswordMatch(password, confirmPassword);
            if (pwErr != null) return new Response(Response.BAD_REQUEST, pwErr);
            doctor.setPassword(password);
        }

        if (!isEmpty(firstname)) doctor.setFirstname(firstname.trim());
        if (!isEmpty(lastname))  doctor.setLastname(lastname.trim());

        // Licence validation
        if (!isEmpty(licenceNumber)) {
            String licenceErr = UserValidator.validateLicenceNumber(licenceNumber);
            if (licenceErr != null) return new Response(Response.BAD_REQUEST, licenceErr);
            doctor.setLicenceNumber(licenceNumber.trim());
        }

        // Office validation
        if (!isEmpty(assignedOffice)) {
            String officeErr = UserValidator.validateOffice(assignedOffice);
            if (officeErr != null) return new Response(Response.BAD_REQUEST, officeErr);
            doctor.setAssignedOffice(assignedOffice.trim());
        }

        if (specialty != null) {
            doctor.setSpecialty(specialty);
        }

        return new Response(Response.OK, "Doctor information updated successfully.");
    }

    // -------------------------------------------------------------------------
    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
