package core.models.repository;

import core.models.Appointment;
import java.util.List;

/**
 * Repository abstraction for appointment persistence.
 * Controllers depend on this interface (DIP), not on DataStore directly.
 */
public interface IAppointmentRepository {
    Appointment findById(String id);
    void save(Appointment appointment);
    List<Appointment> findAll();
    List<Appointment> findByPatientId(long patientId);
    List<Appointment> findByDoctorId(long doctorId);
    String generateId(long patientId);
}
