package core.models.storage;

import core.models.Appointment;
import core.models.DataStore;
import core.models.repository.IAppointmentRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of IAppointmentRepository backed by the DataStore singleton.
 */
public class InMemoryAppointmentRepository implements IAppointmentRepository {

    private final DataStore dataStore;

    public InMemoryAppointmentRepository() {
        this.dataStore = DataStore.getInstance();
    }

    @Override
    public Appointment findById(String id) {
        return dataStore.findAppointmentById(id);
    }

    @Override
    public void save(Appointment appointment) {
        dataStore.addAppointment(appointment);
    }

    @Override
    public List<Appointment> findAll() {
        return new ArrayList<>(dataStore.getAppointments());
    }

    @Override
    public List<Appointment> findByPatientId(long patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : dataStore.getAppointments()) {
            if (a.getPatient() != null && a.getPatient().getId() == patientId) {
                result.add(a);
            }
        }
        return result;
    }

    @Override
    public List<Appointment> findByDoctorId(long doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : dataStore.getAppointments()) {
            if (a.getDoctor() != null && a.getDoctor().getId() == doctorId) {
                result.add(a);
            }
        }
        return result;
    }

    @Override
    public String generateId(long patientId) {
        return dataStore.generateAppointmentId(patientId);
    }
}
