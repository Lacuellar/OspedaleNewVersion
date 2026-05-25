package core.models.repository;

import core.models.Hospitalization;
import java.util.List;

/**
 * Repository abstraction for hospitalization persistence.
 * Controllers depend on this interface (DIP), not on DataStore directly.
 */
public interface IHospitalizationRepository {
    Hospitalization findById(String id);
    void save(Hospitalization hospitalization);
    List<Hospitalization> findAll();
    List<Hospitalization> findByPatientId(long patientId);
    List<Hospitalization> findByDoctorId(long doctorId);
    String generateId(long patientId);
}
