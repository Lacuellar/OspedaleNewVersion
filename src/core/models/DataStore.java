package core.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataStore {

    private static DataStore instance;
    private ArrayList<Administrator> admins;
    private ArrayList<Patient> patients;
    private ArrayList<Doctor> doctors;
    private ArrayList<Appointment> appointments;
    private ArrayList<Hospitalization> hospitalizations;
    private ArrayList<DataObserver> observers;

    private DataStore() {
        admins = new ArrayList<>();
        patients = new ArrayList<>();
        doctors = new ArrayList<>();
        appointments = new ArrayList<>();
        hospitalizations = new ArrayList<>();
        observers = new ArrayList<>();
        loadData();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    /**
     * Two-phase loading:
     *   Phase 1 — users.json: load admins, patients, doctors (primitive fields only)
     *   Phase 2a — appointments.json: wire appointments to already-loaded Patient/Doctor
     *   Phase 2b — hospitalizations.json: wire hospitalizations to Patient/Doctor
     */
    private void loadData() {
        loadUsers();
        loadAppointments();
        loadHospitalizations();
    }

    /** Phase 1: Load all users from json/users.json. */
    private void loadUsers() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("json/users.json")));
            JSONObject root = new JSONObject(content);
            JSONArray users = root.getJSONArray("users");

            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                String type = user.getString("type");

                switch (type) {
                    case "admin":
                        admins.add(new Administrator(
                                user.getLong("id"),
                                user.getString("username"),
                                user.getString("firstname"),
                                user.getString("lastname"),
                                user.getString("password")
                        ));
                        break;
                    case "patient":
                        patients.add(new Patient(
                                user.getLong("id"),
                                user.getString("username"),
                                user.getString("firstname"),
                                user.getString("lastname"),
                                user.getString("password"),
                                user.getString("email"),
                                LocalDate.parse(user.getString("birthdate")),
                                user.getBoolean("gender"),
                                user.getLong("phone"),
                                user.getString("address")
                        ));
                        break;
                    case "doctor":
                        doctors.add(new Doctor(
                                user.getLong("id"),
                                user.getString("username"),
                                user.getString("firstname"),
                                user.getString("lastname"),
                                user.getString("password"),
                                normalizeSpecialty(user.getString("specialty")),
                                user.getString("licenceNumber"),
                                user.getString("assignedOffice")
                        ));
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("[DataStore] Could not load users.json: " + e.getMessage());
        }
    }

    /**
     * Phase 2a: Load appointments from json/appointments.json.
     * Patients and doctors must already be loaded (Phase 1 completed).
     * Each appointment entry: {id, patientId, doctorId, specialty, datetime, reason, type, status,
     *                          diagnosis?, observations?, treatment?, followUp?}
     */
    private void loadAppointments() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("json/appointments.json")));
            JSONObject root = new JSONObject(content);
            JSONArray arr = root.optJSONArray("appointments");
            if (arr == null) return;

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Patient patient = findPatientById(obj.getLong("patientId"));
                Doctor  doctor  = findDoctorById(obj.getLong("doctorId"));
                if (patient == null || doctor == null) continue;  // skip orphaned records

                java.time.LocalDateTime datetime =
                        java.time.LocalDateTime.parse(obj.getString("datetime"));
                Specialty specialty = normalizeSpecialty(obj.getString("specialty"));
                boolean isInPerson  = obj.optBoolean("type", true);

                Appointment appt = new Appointment(
                        obj.getString("id"), patient, doctor,
                        specialty, datetime, obj.getString("reason"), isInPerson);

                // Restore status (constructor sets REQUESTED; override if different)
                String statusStr = obj.optString("status", "REQUESTED");
                try { appt.setStatus(AppointmentStatus.valueOf(statusStr)); }
                catch (IllegalArgumentException ignored) {}

                // Restore medical notes
                appt.setDiagnosis(obj.optString("diagnosis", ""));
                appt.setObservations(obj.optString("observations", ""));
                appt.setRecommendedTreatment(obj.optString("treatment", ""));
                appt.setFollowUp(obj.optString("followUp", ""));

                appointments.add(appt);
            }
        } catch (IOException e) {
            System.err.println("[DataStore] Could not load appointments.json: " + e.getMessage());
        }
    }

    /**
     * Phase 2b: Load hospitalizations from json/hospitalizations.json.
     * Patients and doctors must already be loaded (Phase 1 completed).
     * Each entry: {id, patientId, doctorId, date, reason, roomType, status, observations?}
     */
    private void loadHospitalizations() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("json/hospitalizations.json")));
            JSONObject root = new JSONObject(content);
            JSONArray arr = root.optJSONArray("hospitalizations");
            if (arr == null) return;

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Patient patient = findPatientById(obj.getLong("patientId"));
                Doctor  doctor  = findDoctorById(obj.getLong("doctorId"));
                if (patient == null || doctor == null) continue;

                LocalDate date = LocalDate.parse(obj.getString("date"));
                RoomType roomType;
                try { roomType = RoomType.valueOf(obj.getString("roomType")); }
                catch (IllegalArgumentException e) { continue; }

                HospitalizationStatus status;
                try { status = HospitalizationStatus.valueOf(obj.optString("status", "REQUESTED")); }
                catch (IllegalArgumentException e) { status = HospitalizationStatus.REQUESTED; }

                Hospitalization hosp = new Hospitalization(
                        obj.getString("id"), patient, doctor, date,
                        obj.getString("reason"), roomType,
                        obj.optString("observations", ""), status);

                hospitalizations.add(hosp);
            }
        } catch (IOException e) {
            System.err.println("[DataStore] Could not load hospitalizations.json: " + e.getMessage());
        }
    }

    /**
     * Normalizes specialty strings from JSON to enum values.
     * Handles aliases: "ORTHOPEDICS" → TRAUMATOLOGY_ORTHOPEDICS,
     *                  "GYNECOLOGY"  → GYNECOLOGY_OBSTETRICS
     */
    private Specialty normalizeSpecialty(String specialtyStr) {
        switch (specialtyStr) {
            case "ORTHOPEDICS": return Specialty.TRAUMATOLOGY_ORTHOPEDICS;
            case "GYNECOLOGY":  return Specialty.GYNECOLOGY_OBSTETRICS;
            default:
                try { return Specialty.valueOf(specialtyStr); }
                catch (IllegalArgumentException e) { return Specialty.GENERAL_MEDICINE; }
        }
    }

    public ArrayList<Administrator> getAdmins() {
        return admins;
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public ArrayList<Hospitalization> getHospitalizations() {
        return hospitalizations;
    }

    // =========================================================================
    // OBSERVER PATTERN (Bonus)
    // =========================================================================

    public void addObserver(DataObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(DataObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String entityType) {
        for (DataObserver observer : new ArrayList<>(observers)) {
            observer.onDataChanged(entityType);
        }
    }

    // =========================================================================
    // MUTATIONS
    // =========================================================================

    public void addAdmin(Administrator admin) {
        admins.add(admin);
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
        notifyObservers("patients");
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
        notifyObservers("doctors");
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        notifyObservers("appointments");
    }

    public void addHospitalization(Hospitalization hospitalization) {
        hospitalizations.add(hospitalization);
        notifyObservers("hospitalizations");
    }

    public Administrator findAdminById(long id) {
        for (Administrator admin : admins) {
            if (admin.getId() == id) {
                return admin;
            }
        }
        return null;
    }

    public Patient findPatientById(long id) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    public Doctor findDoctorById(long id) {
        for (Doctor doctor : doctors) {
            if (doctor.getId() == id) {
                return doctor;
            }
        }
        return null;
    }

    public Appointment findAppointmentById(String id) {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(id)) {
                return appointment;
            }
        }
        return null;
    }

    public Hospitalization findHospitalizationById(String id) {
        for (Hospitalization hospitalization : hospitalizations) {
            if (hospitalization.getId().equals(id)) {
                return hospitalization;
            }
        }
        return null;
    }

    public String generateAppointmentId(long patientId) {
        Patient patient = findPatientById(patientId);
        int count = 0;
        if (patient != null) {
            count = patient.getAppointments().size();
        }
        return String.format("A-%d-%04d", patientId, count);
    }

    public String generateHospitalizationId(long patientId) {
        int count = 0;
        for (Hospitalization h : hospitalizations) {
            if (h.getPatient().getId() == patientId) {
                count++;
            }
        }
        return String.format("H-%d-%04d", patientId, count);
    }

    public boolean usernameExists(String username) {
        return getUserByUsername(username) != null;
    }

    public boolean userIdExists(long id) {
        return findAdminById(id) != null || findPatientById(id) != null || findDoctorById(id) != null;
    }

    public User getUserByUsername(String username) {
        for (Administrator admin : admins) {
            if (admin.getUsername().equals(username)) {
                return admin;
            }
        }
        for (Patient patient : patients) {
            if (patient.getUsername().equals(username)) {
                return patient;
            }
        }
        for (Doctor doctor : doctors) {
            if (doctor.getUsername().equals(username)) {
                return doctor;
            }
        }
        return null;
    }
}
