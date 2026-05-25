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
import core.models.Specialty;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Read-only controller for populating combo boxes in the views.
 *
 * Satisfies SRP: all "give me items for a dropdown" queries live here,
 * separated from business-action controllers and table-data controllers.
 * Views call these methods and receive JSON — no model objects cross the boundary.
 */
public class ComboDataController {

    private final DataStore dataStore;

    public ComboDataController() {
        this.dataStore = DataStore.getInstance();
    }

    // =========================================================================
    // SPECIALTIES COMBO
    // =========================================================================

    /**
     * Returns all available medical specialties as a JSON array of {name, displayName}.
     */
    public Response getSpecialties() {
        JSONArray arr = new JSONArray();
        for (Specialty s : Specialty.values()) {
            JSONObject obj = new JSONObject();
            obj.put("name",        s.name());
            obj.put("displayName", s.name().replace("_", " "));
            arr.put(obj);
        }
        JSONObject data = new JSONObject();
        data.put("specialties", arr);
        return new Response(Response.OK, arr.length() + " specialties found.", data);
    }

    // =========================================================================
    // DOCTORS COMBO
    // =========================================================================

    /**
     * Returns all doctors as {id, fullName, specialty} for combo boxes.
     */
    public Response getAllDoctors() {
        JSONArray arr = new JSONArray();
        for (Doctor d : dataStore.getDoctors()) {
            arr.put(doctorEntry(d));
        }
        JSONObject data = new JSONObject();
        data.put("doctors", arr);
        return new Response(Response.OK, arr.length() + " doctor(s) found.", data);
    }

    /**
     * Returns doctors filtered by specialty as {id, fullName, specialty}.
     */
    public Response getDoctorsBySpecialty(String specialtyName) {
        Specialty specialty;
        try {
            specialty = Specialty.valueOf(specialtyName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new Response(Response.BAD_REQUEST, "Unknown specialty: " + specialtyName);
        }
        JSONArray arr = new JSONArray();
        for (Doctor d : dataStore.getDoctors()) {
            if (d.getSpecialty() == specialty) {
                arr.put(doctorEntry(d));
            }
        }
        JSONObject data = new JSONObject();
        data.put("doctors", arr);
        return new Response(Response.OK, arr.length() + " doctor(s) found for specialty " + specialtyName + ".", data);
    }

    // =========================================================================
    // ROOM TYPES COMBO
    // =========================================================================

    /**
     * Returns all room types as {name, displayName} for combo boxes.
     */
    public Response getRoomTypes() {
        JSONArray arr = new JSONArray();
        for (RoomType rt : RoomType.values()) {
            JSONObject obj = new JSONObject();
            obj.put("name",        rt.name());
            obj.put("displayName", rt.name().replace("_", " "));
            arr.put(obj);
        }
        JSONObject data = new JSONObject();
        data.put("roomTypes", arr);
        return new Response(Response.OK, arr.length() + " room types found.", data);
    }

    // =========================================================================
    // APPOINTMENT IDS COMBO
    // =========================================================================

    /**
     * Returns appointment IDs for a patient filtered by status(es).
     * @param statuses comma-separated status names, e.g. "REQUESTED,PENDING", or "ALL"
     */
    public Response getPatientAppointmentIds(long patientId, String statuses) {
        Patient patient = dataStore.findPatientById(patientId);
        if (patient == null) {
            return new Response(Response.NOT_FOUND, "Patient not found.");
        }
        JSONArray arr = new JSONArray();
        for (Appointment appt : patient.getAppointments()) {
            if (matchesStatus(appt.getStatus().name(), statuses)) {
                arr.put(appt.getId());
            }
        }
        JSONObject data = new JSONObject();
        data.put("appointmentIds", arr);
        return new Response(Response.OK, arr.length() + " appointment ID(s) found.", data);
    }

    /**
     * Returns hospitalization IDs for a patient filtered by status(es).
     */
    public Response getPatientHospitalizationIds(long patientId, String statuses) {
        Patient patient = dataStore.findPatientById(patientId);
        if (patient == null) {
            return new Response(Response.NOT_FOUND, "Patient not found.");
        }
        JSONArray arr = new JSONArray();
        for (Hospitalization h : dataStore.getHospitalizations()) {
            if (h.getPatient() != null && h.getPatient().getId() == patientId) {
                if (matchesStatus(h.getStatus().name(), statuses)) {
                    arr.put(h.getId());
                }
            }
        }
        JSONObject data = new JSONObject();
        data.put("hospitalizationIds", arr);
        return new Response(Response.OK, arr.length() + " hospitalization ID(s) found.", data);
    }

    /**
     * Returns appointment IDs for a doctor filtered by status(es).
     */
    public Response getDoctorAppointmentIds(long doctorId, String statuses) {
        if (dataStore.findDoctorById(doctorId) == null) {
            return new Response(Response.NOT_FOUND, "Doctor not found.");
        }
        JSONArray arr = new JSONArray();
        for (Appointment appt : dataStore.getAppointments()) {
            if (appt.getDoctor() != null && appt.getDoctor().getId() == doctorId) {
                if (matchesStatus(appt.getStatus().name(), statuses)) {
                    arr.put(appt.getId());
                }
            }
        }
        JSONObject data = new JSONObject();
        data.put("appointmentIds", arr);
        return new Response(Response.OK, arr.length() + " appointment ID(s) found.", data);
    }

    /**
     * Returns hospitalization IDs for a doctor filtered by status(es).
     */
    public Response getDoctorHospitalizationIds(long doctorId, String statuses) {
        if (dataStore.findDoctorById(doctorId) == null) {
            return new Response(Response.NOT_FOUND, "Doctor not found.");
        }
        JSONArray arr = new JSONArray();
        for (Hospitalization h : dataStore.getHospitalizations()) {
            if (h.getDoctor() != null && h.getDoctor().getId() == doctorId) {
                if (matchesStatus(h.getStatus().name(), statuses)) {
                    arr.put(h.getId());
                }
            }
        }
        JSONObject data = new JSONObject();
        data.put("hospitalizationIds", arr);
        return new Response(Response.OK, arr.length() + " hospitalization ID(s) found.", data);
    }

    // =========================================================================
    // PATIENTS COMBO
    // =========================================================================

    /**
     * Returns all patients as {id, fullName} for combo boxes.
     */
    public Response getAllPatients() {
        JSONArray arr = new JSONArray();
        for (Patient p : dataStore.getPatients()) {
            JSONObject obj = new JSONObject();
            obj.put("id",       p.getId());
            obj.put("fullName", p.getFirstname() + " " + p.getLastname());
            arr.put(obj);
        }
        JSONObject data = new JSONObject();
        data.put("patients", arr);
        return new Response(Response.OK, arr.length() + " patient(s) found.", data);
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private JSONObject doctorEntry(Doctor d) {
        JSONObject obj = new JSONObject();
        obj.put("id",        d.getId());
        obj.put("fullName",  d.getFirstname() + " " + d.getLastname());
        obj.put("specialty", d.getSpecialty() != null ? d.getSpecialty().name() : "");
        return obj;
    }

    private boolean matchesStatus(String statusName, String filter) {
        if (filter == null || "ALL".equalsIgnoreCase(filter)) return true;
        for (String s : filter.split(",")) {
            if (s.trim().equalsIgnoreCase(statusName)) return true;
        }
        return false;
    }
}
