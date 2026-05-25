package core.controllers;

import core.models.Response;

/**
 * Interface for patient controller (SOLID - Interface Segregation Principle).
 */
public interface IPatientController {
    Response registerPatient(String idStr, String username, String firstname, String lastname,
                             String password, String confirmPassword,
                             String email, String birthdateStr, String genderStr,
                             String phoneStr, String address);

    Response updatePatient(long patientId, String firstname, String lastname,
                           String password, String confirmPassword,
                           String email, String birthdateStr, String genderStr,
                           String phoneStr, String address);
}
