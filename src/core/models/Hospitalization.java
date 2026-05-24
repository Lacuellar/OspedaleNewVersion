package core.models;

import core.models.Doctor;
import core.models.Patient;
import java.time.LocalDate;
import core.models.HospitalizationStatus;
import core.models.RoomType;

public class Hospitalization {
    
    private final String id;
    private Patient patient;
    private Doctor doctor;
    private LocalDate date;

    public String getId() {
        return id;
    }
    private String reason;
    private RoomType roomType;
    private String observations;
    private HospitalizationStatus status;

    public void setStatus(HospitalizationStatus status) {
        this.status = status;
    }

    public Hospitalization(String id, Patient patient, Doctor doctor, LocalDate date, String reason, RoomType roomType, String observations) {
        this.id = id;
        this.patient = patient;
        patient.setHospitalization(this);
        this.doctor = doctor;
        doctor.addHospitalization(this);
        this.date = date;
        this.reason = reason;
        this.roomType = roomType;
        this.observations = observations;
        this.status = HospitalizationStatus.REQUESTED;
    }
    public Hospitalization(String id, Patient patient, Doctor doctor, LocalDate date, String reason, RoomType roomType, String observations, HospitalizationStatus hopsS) {
        this.id = id;
        this.patient = patient;
        patient.setHospitalization(this);
        this.doctor = doctor;
        doctor.addHospitalization(this);
        this.date = date;
        this.reason = reason;
        this.roomType = roomType;
        this.observations = observations;
        this.status = hopsS;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public String getObservations() {
        return observations;
    }

    public HospitalizationStatus getStatus() {
        return status;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

}
