package core.controllers;

import core.models.Appointment;
import core.models.AppointmentStatus;
import core.models.DataStore;
import core.models.Hospitalization;
import core.models.Patient;
import core.models.Prescription;
import core.models.Response;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Read-only controller for populating tables in the views.
 *
 * Satisfies SRP: all "give me data for a table" queries live here,
 * separated from the business-action controllers.
 * Views call these methods and receive JSON — no model objects cross the boundary.
 */
public class TableDataController {

    private final DataStore dataStore;

    public TableDataController() {
        this.dataStore = DataStore.getInstance();
    }

    // =========================================================================
    // PATIENT APPOINTMENTS TABLE
    // =========================================================================

    /**
     * Returns all appointments for a patient, sorted descending by datetime.
     * Each row: {id, datetime, doctorName, specialty, type, status}
     */
    public Response getPatientAppointments(long patientId) {
        Patient patient = dataStore.findPatientById(patientId);
        if (patient == null) {
            return new Response(Response.NOT_FOUND, "Patient not found.");
        }
        List<Appointment> list = new ArrayList<>(patient.getAppointments());
        list.sort(Comparator.comparing(Appointment::getDatetime).reversed());

        JSONArray arr = new JSONArray();
        for (Appointment a : list) {
            JSONObject obj = new JSONObject();
            obj.put("id",         a.getId());
            obj.put("datetime",   a.getDatetime() != null ? a.getDatetime().toString() : "");
            obj.put("doctorName", a.getDoctor()   != null
                    ? a.getDoctor().getFirstname() + " " + a.getDoctor().getLastname() : "");
            obj.put("specialty",  a.getSpecialty() != null ? a.getSpecialty().name() : "");
            obj.put("type",       a.isType() ? "In-person" : "Remote");
            obj.put("status",     a.getStatus() != null ? a.getStatus().name() : "");
            arr.put(obj);
        }
        JSONObject data = new JSONObject();
        data.put("appointments", arr);
        return new Response(Response.OK, arr.length() + " appointment(s) found.", data);
    }

    // =========================================================================
    // DOCTOR APPOINTMENTS TABLE
    // =========================================================================

    /**
     * Returns appointments for a doctor, filtered by status.
     *
     * @param filter  "ALL" | "PENDING_ONLY" (REQUESTED + PENDING) | status name
     * Each row: {id, datetime, patientName, specialty, type, status}
     */
    public Response getDoctorAppointments(long doctorId, String filter) {
        if (dataStore.findDoctorById(doctorId) == null) {
            return new Response(Response.NOT_FOUND, "Doctor not found.");
        }
        List<Appointment> list = new ArrayList<>();
        for (Appointment a : dataStore.getAppointments()) {
            if (a.getDoctor() != null && a.getDoctor().getId() == doctorId) {
                list.add(a);
            }
        }
        list.sort(Comparator.comparing(Appointment::getDatetime).reversed());

        JSONArray arr = new JSONArray();
        for (Appointment a : list) {
            if (!matchesFilter(a.getStatus(), filter)) continue;
            JSONObject obj = new JSONObject();
            obj.put("id",          a.getId());
            obj.put("datetime",    a.getDatetime() != null ? a.getDatetime().toString() : "");
            obj.put("patientName", a.getPatient()  != null
                    ? a.getPatient().getFirstname() + " " + a.getPatient().getLastname() : "");
            obj.put("specialty",   a.getSpecialty() != null ? a.getSpecialty().name() : "");
            obj.put("type",        a.isType() ? "In-person" : "Remote");
            obj.put("status",      a.getStatus() != null ? a.getStatus().name() : "");
            arr.put(obj);
        }
        JSONObject data = new JSONObject();
        data.put("appointments", arr);
        return new Response(Response.OK, arr.length() + " appointment(s) found.", data);
    }

    // =========================================================================
    // HOSPITALIZATION TABLES
    // =========================================================================

    /**
     * Returns all hospitalizations (admin view).
     * Each row: {id, patientName, doctorName, date, reason, roomType, status}
     */
    public Response getHospitalizationRequests() {
        JSONArray arr = new JSONArray();
        for (Hospitalization h : dataStore.getHospitalizations()) {
            arr.put(serializeHospitalization(h));
        }
        JSONObject data = new JSONObject();
        data.put("hospitalizations", arr);
        return new Response(Response.OK, arr.length() + " hospitalization(s) found.", data);
    }

    /**
     * Returns hospitalizations for a specific patient.
     */
    public Response getHospitalizationsByPatient(long patientId) {
        if (dataStore.findPatientById(patientId) == null) {
            return new Response(Response.NOT_FOUND, "Patient not found.");
        }
        JSONArray arr = new JSONArray();
        for (Hospitalization h : dataStore.getHospitalizations()) {
            if (h.getPatient() != null && h.getPatient().getId() == patientId) {
                arr.put(serializeHospitalization(h));
            }
        }
        JSONObject data = new JSONObject();
        data.put("hospitalizations", arr);
        return new Response(Response.OK, arr.length() + " hospitalization(s) found.", data);
    }

    // =========================================================================
    // PRESCRIPTIONS TABLE
    // =========================================================================

    /**
     * Returns all prescriptions for a given appointment.
     * Each row: {medication, dose, route, duration, frequency, instructions}
     */
    public Response getPrescriptions(String appointmentId) {
        Appointment appt = dataStore.findAppointmentById(appointmentId);
        if (appt == null) {
            return new Response(Response.NOT_FOUND, "Appointment not found.");
        }
        JSONArray arr = new JSONArray();
        for (Prescription p : appt.getPrescriptions()) {
            JSONObject obj = new JSONObject();
            obj.put("medication",   p.getMedicationName());
            obj.put("dose",         p.getDose());
            obj.put("route",        p.getAdministrationRoute());
            obj.put("duration",     p.getTreatmentDuration());
            obj.put("frequency",    p.getFrecuency());
            obj.put("instructions", p.getAdditionalInstructions() != null ? p.getAdditionalInstructions() : "");
            arr.put(obj);
        }
        JSONObject data = new JSONObject();
        data.put("prescriptions", arr);
        return new Response(Response.OK, arr.length() + " prescription(s) found.", data);
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private boolean matchesFilter(AppointmentStatus status, String filter) {
        if (filter == null || "ALL".equalsIgnoreCase(filter)) return true;
        if ("PENDING_ONLY".equalsIgnoreCase(filter)) {
            return status == AppointmentStatus.REQUESTED || status == AppointmentStatus.PENDING;
        }
        try {
            return status == AppointmentStatus.valueOf(filter.toUpperCase());
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private JSONObject serializeHospitalization(Hospitalization h) {
        JSONObject obj = new JSONObject();
        obj.put("id",          h.getId());
        obj.put("patientName", h.getPatient() != null
                ? h.getPatient().getFirstname() + " " + h.getPatient().getLastname() : "");
        obj.put("doctorName",  h.getDoctor() != null
                ? h.getDoctor().getFirstname() + " " + h.getDoctor().getLastname() : "");
        obj.put("date",        h.getDate() != null ? h.getDate().toString() : "");
        obj.put("reason",      h.getReason() != null ? h.getReason() : "");
        obj.put("roomType",    h.getRoomType() != null ? h.getRoomType().name() : "");
        obj.put("status",      h.getStatus() != null ? h.getStatus().name() : "");
        return obj;
    }
}
