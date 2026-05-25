package core.models.repository;

import core.models.Patient;
import java.util.List;

/**
 * Repository abstraction for patient persistence.
 * Controllers depend on this interface (DIP), not on DataStore directly.
 */
public interface IPatientRepository {
    Patient findById(long id);
    void save(Patient patient);
    List<Patient> findAll();
}
