package core.controllers;

import core.models.DataStore;
import core.models.Doctor;
import core.models.Hospitalization;
import core.models.HospitalizationStatus;
import core.models.Patient;
import core.models.Response;
import core.models.RoomType;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.json.JSONObject;

/**
 * Controller for hospitalization-related operations.
 *
 * Business rules enforced:
 *   - Patient and doctor must exist
 *   - Date must be a valid YYYY-MM-DD
 *   - Status transitions: REQUESTED → ONGOING, REQUESTED → CANCELED
 *   - Cannot cancel an ONGOING hospitalization
 *
 * Views must NOT implement any of this logic — call the controller and display the Response.
 */
public class HospitalizationController {

    private final DataStore dataStore;

    public HospitalizationController() {
        this.dataStore = DataStore.getInstance();
    }

 

    /**
     * Creates a new hospitalization request.
     *
     * @param patientId    patient's numeric ID
     * @param doctorId     doctor's numeric ID
     * @param dateStr      admission date "YYYY-MM-DD"
     * @param reason       reason for hospitalization
     * @param roomType     requested room type
     * @param observations optional observations
     * @return Response(CREATED, ..., data{hospitalizationId, doctorName}) on success.
     */
    public Response requestHospitalization(long patientId, long doctorId,
                                            String dateStr, String reason,
                                            RoomType roomType, String observations) {

        if (isEmpty(dateStr) || isEmpty(reason) || roomType == null) {
            return new Response(Response.BAD_REQUEST,
                    "Please fill all required fields (date, reason, room type).");
        }

        Patient patient = dataStore.findPatientById(patientId);
        if (patient == null) {
            return new Response(Response.NOT_FOUND, "Patient not found.");
        }

        Doctor doctor = dataStore.findDoctorById(doctorId);
        if (doctor == null) {
            return new Response(Response.NOT_FOUND, "Doctor not found. Please select a valid doctor.");
        }

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr.trim());
        } catch (DateTimeParseException e) {
            return new Response(Response.BAD_REQUEST,
                    "Invalid date format. Use YYYY-MM-DD (e.g., 2026-06-01).");
        }

        String hospId = dataStore.generateHospitalizationId(patientId);
        Hospitalization hosp = new Hospitalization(
                hospId, patient, doctor, date, reason.trim(),
                roomType, isEmpty(observations) ? "" : observations.trim());
        dataStore.addHospitalization(hosp);

        JSONObject data = new JSONObject();
        data.put("hospitalizationId", hospId);
        data.put("doctorName", doctor.getFirstname() + " " + doctor.getLastname());
        data.put("date", dateStr);

        return new Response(Response.CREATED,
                "Hospitalization " + hospId + " requested with Dr. "
                + doctor.getFirstname() + " " + doctor.getLastname()
                + " on " + dateStr + ".", data);
    }

    // =========================================================================
    // STATUS TRANSITIONS
    // =========================================================================

    /**
     * Accepts (begins) a hospitalization: REQUESTED → ONGOING.
     */
    public Response acceptHospitalization(String hospId) {
        Hospitalization hosp = dataStore.findHospitalizationById(hospId);
        if (hosp == null) {
            return new Response(Response.NOT_FOUND,
                    "Hospitalization '" + hospId + "' not found.");
        }
        if (hosp.getStatus() != HospitalizationStatus.REQUESTED) {
            return new Response(Response.CONFLICT,
                    "Hospitalization must be in REQUESTED status to accept. Current: " + hosp.getStatus());
        }
        hosp.setStatus(HospitalizationStatus.ONGOING);
        return new Response(Response.OK,
                "Hospitalization " + hospId + " accepted and is now ONGOING.");
    }

    /**
     * Cancels a hospitalization: REQUESTED → CANCELED.
     * Cannot cancel an ONGOING hospitalization.
     */
    public Response cancelHospitalization(String hospId) {
        Hospitalization hosp = dataStore.findHospitalizationById(hospId);
        if (hosp == null) {
            return new Response(Response.NOT_FOUND,
                    "Hospitalization '" + hospId + "' not found.");
        }
        if (hosp.getStatus() == HospitalizationStatus.CANCELED) {
            return new Response(Response.CONFLICT,
                    "Hospitalization is already CANCELED.");
        }
        if (hosp.getStatus() == HospitalizationStatus.ONGOING) {
            return new Response(Response.CONFLICT,
                    "Cannot cancel an ONGOING hospitalization.");
        }
        hosp.setStatus(HospitalizationStatus.CANCELED);
        return new Response(Response.OK,
                "Hospitalization " + hospId + " has been canceled.");
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
