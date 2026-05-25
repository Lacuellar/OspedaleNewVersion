package core.models.repository;

import core.models.Doctor;
import core.models.Specialty;
import java.util.List;

/**
 * Repository abstraction for doctor persistence and specialty queries.
 * Controllers depend on this interface (DIP), not on DataStore directly.
 */
public interface IDoctorRepository {
    Doctor findById(long id);
    void save(Doctor doctor);
    List<Doctor> findAll();
    List<Doctor> findBySpecialty(Specialty specialty);
}
