package core.models.storage;

import core.models.DataStore;
import core.models.Doctor;
import core.models.Specialty;
import core.models.repository.IDoctorRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of IDoctorRepository backed by the DataStore singleton.
 */
public class InMemoryDoctorRepository implements IDoctorRepository {

    private final DataStore dataStore;

    public InMemoryDoctorRepository() {
        this.dataStore = DataStore.getInstance();
    }

    @Override
    public Doctor findById(long id) {
        return dataStore.findDoctorById(id);
    }

    @Override
    public void save(Doctor doctor) {
        dataStore.addDoctor(doctor);
    }

    @Override
    public List<Doctor> findAll() {
        return new ArrayList<>(dataStore.getDoctors());
    }

    @Override
    public List<Doctor> findBySpecialty(Specialty specialty) {
        List<Doctor> result = new ArrayList<>();
        for (Doctor d : dataStore.getDoctors()) {
            if (d.getSpecialty() == specialty) {
                result.add(d);
            }
        }
        return result;
    }
}
