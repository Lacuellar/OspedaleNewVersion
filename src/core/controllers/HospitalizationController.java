package core.controllers;

import core.models.Appointment;
import core.models.AppointmentStatus;
import core.models.DataStore;
import core.models.Doctor;
import core.models.Hospitalization;
import core.models.HospitalizationStatus;
import core.models.Patient;
import core.models.Response;
import core.models.RoomType;
import java.time.LocalDate;
import org.json.JSONArray;
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
public class HospitalizationController implements IHospitalizationController {

    private final DataStore dataStore;

    public HospitalizationController() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Converts a COMPLETED appointment into a new ONGOING hospitalization.
     *
     * Rules enforced:
     *   - The appointment must exist
     *   - The appointment must be in COMPLETED status (cannot hospitalize pending cases)
     *   - reason, roomType and dateStr are required
     *   - Creates a new Hospitalization with status ONGOING
     *
     * @param appointmentId the completed appointment to convert
     * @param dateStr       admission date "YYYY-MM-DD"
     * @param reason        reason for hospitalization
     * @param roomType      requested room type
     * @param observations  optional observations
     * @return Response(CREATED, ..., data{hospitalizationId}) on success.
     */
    @Override
    public Response fromAppointment(String appointmentId, String dateStr,
                                     String reason, RoomType roomType, String observations) {
        Appointment appt = dataStore.findAppointmentById(appointmentId);
        if (appt == null) {
            return new Response(Response.NOT_FOUND,
                    "Appointment '" + appointmentId + "' not found.");
        }
        if (appt.getStatus() != AppointmentStatus.COMPLETED) {
            return new Response(Response.CONFLICT,
                    "Only COMPLETED appointments can be converted to a hospitalization. "
                    + "Current status: " + appt.getStatus());
        }

        // Validate required fields via HospitalizationValidator (SRP)
        String[] errOut = {null};
        LocalDate date = HospitalizationValidator.parseAndValidateDate(dateStr, errOut);
        if (date == null) return new Response(Response.BAD_REQUEST, errOut[0]);

        String fieldErr = HospitalizationValidator.validateRequiredFields(reason, roomType);
        if (fieldErr != null) return new Response(Response.BAD_REQUEST, fieldErr);

        Patient patient = appt.getPatient();
        Doctor doctor  = appt.getDoctor();

        String hospId = dataStore.generateHospitalizationId(patient.getId());
        Hospitalization hosp = new Hospitalization(
                hospId, patient, doctor, date, reason.trim(),
                roomType, isEmpty(observations) ? "" : observations.trim());
        hosp.setStatus(HospitalizationStatus.ONGOING);   // auto-accepted since appointment was completed
        dataStore.addHospitalization(hosp);

        JSONObject data = new JSONObject();
        data.put("hospitalizationId", hospId);
        data.put("fromAppointmentId", appointmentId);
        data.put("date", dateStr);

        return new Response(Response.CREATED,
                "Hospitalization " + hospId + " created (ONGOING) from appointment "
                + appointmentId + ".", data);
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
    @Override
    public Response requestHospitalization(long patientId, long doctorId,
                                            String dateStr, String reason,
                                            RoomType roomType, String observations) {

        // Validate required fields via HospitalizationValidator (SRP)
        String fieldErr = HospitalizationValidator.validateRequiredFields(reason, roomType);
        if (fieldErr != null) return new Response(Response.BAD_REQUEST, fieldErr);

        Patient patient = dataStore.findPatientById(patientId);
        if (patient == null) {
            return new Response(Response.NOT_FOUND, "Patient not found.");
        }

        Doctor doctor = dataStore.findDoctorById(doctorId);
        if (doctor == null) {
            return new Response(Response.NOT_FOUND, "Doctor not found. Please select a valid doctor.");
        }

        String[] errOut = {null};
        LocalDate date = HospitalizationValidator.parseAndValidateDate(dateStr, errOut);
        if (date == null) return new Response(Response.BAD_REQUEST, errOut[0]);

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
    @Override
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
        dataStore.notifyObservers("hospitalizations");
        return new Response(Response.OK,
                "Hospitalization " + hospId + " accepted and is now ONGOING.");
    }

    /**
     * Cancels a hospitalization: REQUESTED → CANCELED.
     * Cannot cancel an ONGOING hospitalization.
     */
    @Override
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
        dataStore.notifyObservers("hospitalizations");
        return new Response(Response.OK,
                "Hospitalization " + hospId + " has been canceled.");
    }

    // =========================================================================
    // QUERIES — serialized (returns JSON in Response, no model objects to views)
    // =========================================================================

    /**
     * Returns all hospitalizations for a given doctor as a serialized JSONArray.
     * The Response data contains key "hospitalizations" → JSONArray.
     */
    @Override
    public Response getDoctorHospitalizationsResponse(long doctorId) {
        Doctor doctor = dataStore.findDoctorById(doctorId);
        if (doctor == null) {
            return new Response(Response.NOT_FOUND, "Doctor not found.");
        }
        JSONArray arr = new JSONArray();
        for (Hospitalization h : dataStore.getHospitalizations()) {
            if (h.getDoctor() != null && h.getDoctor().getId() == doctorId) {
                JSONObject obj = new JSONObject();
                obj.put("id", h.getId());
                obj.put("patientName", h.getPatient() != null
                        ? h.getPatient().getFirstname() + " " + h.getPatient().getLastname() : "");
                obj.put("date", h.getDate() != null ? h.getDate().toString() : "");
                obj.put("reason", h.getReason() != null ? h.getReason() : "");
                obj.put("roomType", h.getRoomType() != null ? h.getRoomType().name() : "");
                obj.put("status", h.getStatus() != null ? h.getStatus().name() : "");
                arr.put(obj);
            }
        }
        JSONObject data = new JSONObject();
        data.put("hospitalizations", arr);
        return new Response(Response.OK,
                "Found " + arr.length() + " hospitalization(s) for doctor " + doctorId + ".", data);
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
