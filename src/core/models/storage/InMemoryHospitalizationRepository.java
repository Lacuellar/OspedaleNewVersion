package core.models.storage;

import core.models.DataStore;
import core.models.Hospitalization;
import core.models.repository.IHospitalizationRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of IHospitalizationRepository backed by the DataStore singleton.
 */
public class InMemoryHospitalizationRepository implements IHospitalizationRepository {

    private final DataStore dataStore;

    public InMemoryHospitalizationRepository() {
        this.dataStore = DataStore.getInstance();
    }

    @Override
    public Hospitalization findById(String id) {
        return dataStore.findHospitalizationById(id);
    }

    @Override
    public void save(Hospitalization hospitalization) {
        dataStore.addHospitalization(hospitalization);
    }

    @Override
    public List<Hospitalization> findAll() {
        return new ArrayList<>(dataStore.getHospitalizations());
    }

    @Override
    public List<Hospitalization> findByPatientId(long patientId) {
        List<Hospitalization> result = new ArrayList<>();
        for (Hospitalization h : dataStore.getHospitalizations()) {
            if (h.getPatient() != null && h.getPatient().getId() == patientId) {
                result.add(h);
            }
        }
        return result;
    }

    @Override
    public List<Hospitalization> findByDoctorId(long doctorId) {
        List<Hospitalization> result = new ArrayList<>();
        for (Hospitalization h : dataStore.getHospitalizations()) {
            if (h.getDoctor() != null && h.getDoctor().getId() == doctorId) {
                result.add(h);
            }
        }
        return result;
    }

    @Override
    public String generateId(long patientId) {
        return dataStore.generateHospitalizationId(patientId);
    }
}
