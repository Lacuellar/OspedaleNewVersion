package core.models.storage;

import core.models.DataStore;
import core.models.Patient;
import core.models.repository.IPatientRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of IPatientRepository backed by the DataStore singleton.
 */
public class InMemoryPatientRepository implements IPatientRepository {

    private final DataStore dataStore;

    public InMemoryPatientRepository() {
        this.dataStore = DataStore.getInstance();
    }

    @Override
    public Patient findById(long id) {
        return dataStore.findPatientById(id);
    }

    @Override
    public void save(Patient patient) {
        dataStore.addPatient(patient);
    }

    @Override
    public List<Patient> findAll() {
        return new ArrayList<>(dataStore.getPatients());
    }
}
