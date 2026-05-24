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

    private DataStore() {
        admins = new ArrayList<>();
        patients = new ArrayList<>();
        doctors = new ArrayList<>();
        appointments = new ArrayList<>();
        hospitalizations = new ArrayList<>();
        loadData();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void loadData() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("json/users.json")));
            JSONObject root = new JSONObject(content);
            JSONArray users = root.getJSONArray("users");

            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                String type = user.getString("type");

                switch (type) {
                    case "admin":
                        Administrator admin = new Administrator(
                                user.getLong("id"),
                                user.getString("username"),
                                user.getString("firstname"),
                                user.getString("lastname"),
                                user.getString("password")
                        );
                        admins.add(admin);
                        break;
                    case "patient":
                        Patient patient = new Patient(
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
                        );
                        patients.add(patient);
                        break;
                    case "doctor":
                        String specialtyStr = user.getString("specialty");
                        Specialty specialty;
                        switch (specialtyStr) {
                            case "ORTHOPEDICS":
                                specialty = Specialty.TRAUMATOLOGY_ORTHOPEDICS;
                                break;
                            case "GYNECOLOGY":
                                specialty = Specialty.GYNECOLOGY_OBSTETRICS;
                                break;
                            default:
                                specialty = Specialty.valueOf(specialtyStr);
                                break;
                        }
                        Doctor doctor = new Doctor(
                                user.getLong("id"),
                                user.getString("username"),
                                user.getString("firstname"),
                                user.getString("lastname"),
                                user.getString("password"),
                                specialty,
                                user.getString("licenceNumber"),
                                user.getString("assignedOffice")
                        );
                        doctors.add(doctor);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public void addAdmin(Administrator admin) {
        admins.add(admin);
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void addHospitalization(Hospitalization hospitalization) {
        hospitalizations.add(hospitalization);
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
