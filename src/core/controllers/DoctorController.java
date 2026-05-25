package core.controllers;

import core.models.DataStore;
import core.models.Doctor;
import core.models.Response;
import core.models.Specialty;
import org.json.JSONObject;

/**
 * Controller for doctor-related operations.
 * Handles registration and profile updates.
 * Views must NOT contain validation logic — delegate to this controller.
 */
public class DoctorController {

    private final DataStore dataStore;

    public DoctorController() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Registers a new doctor with full validation.
     *
     * Validations:
     *   - Required fields must not be empty
     *   - ID must be exactly 12 numeric digits
     *   - Licence number must match: L-XXXXXXXXXX MTL  (L- + 10 digits + space + MTL)
     *   - Office must match: O-XXX  (O- + exactly 3 digits)
     *   - ID and username must be unique
     *
     * @return Response(CREATED) on success; Response(BAD_REQUEST/CONFLICT) on failure.
     */
    public Response registerDoctor(String idStr, String username, String firstname, String lastname,
                                    String password, Specialty specialty,
                                    String licenceNumber, String assignedOffice) {

        if (isEmpty(firstname) || isEmpty(lastname) || isEmpty(idStr)
                || isEmpty(username) || isEmpty(password)
                || specialty == null || isEmpty(licenceNumber) || isEmpty(assignedOffice)) {
            return new Response(Response.BAD_REQUEST,
                    "Please fill all required fields.");
        }

        // ID: exactly 12 digits
        if (!idStr.matches("\\d{12}")) {
            return new Response(Response.BAD_REQUEST,
                    "ID must be exactly 12 numeric digits (e.g., 123456789012).");
        }

        // Licence format: L-XXXXXXXXXX MTL
        if (!licenceNumber.matches("L-\\d{10} MTL")) {
            return new Response(Response.BAD_REQUEST,
                    "Licence number must follow format: L-XXXXXXXXXX MTL\n(e.g., L-1234567890 MTL)");
        }

        // Office format: O-XXX (O- + 3 digits)
        if (!assignedOffice.matches("O-\\d{3}")) {
            return new Response(Response.BAD_REQUEST,
                    "Office must follow format: O-XXX (e.g., O-101, O-204)");
        }

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
     *
     * @return Response(OK) on success; Response(BAD_REQUEST/NOT_FOUND) on failure.
     */
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
            if (!password.equals(confirmPassword)) {
                return new Response(Response.BAD_REQUEST, "Passwords do not match.");
            }
            doctor.setPassword(password);
        }

        if (!isEmpty(firstname)) doctor.setFirstname(firstname.trim());
        if (!isEmpty(lastname))  doctor.setLastname(lastname.trim());

        // Licence validation
        if (!isEmpty(licenceNumber)) {
            if (!licenceNumber.matches("L-\\d{10} MTL")) {
                return new Response(Response.BAD_REQUEST,
                        "Licence number must follow format: L-XXXXXXXXXX MTL");
            }
            doctor.setLicenceNumber(licenceNumber.trim());
        }

        // Office validation
        if (!isEmpty(assignedOffice)) {
            if (!assignedOffice.matches("O-\\d{3}")) {
                return new Response(Response.BAD_REQUEST,
                        "Office must follow format: O-XXX (e.g., O-101)");
            }
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
