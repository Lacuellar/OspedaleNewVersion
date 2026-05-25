package core.controllers;

import core.models.Response;
import core.models.Specialty;

/**
 * Interface for doctor controller (SOLID - Interface Segregation Principle).
 */
public interface IDoctorController {
    Response registerDoctor(String idStr, String username, String firstname, String lastname,
                            String password, String confirmPassword, Specialty specialty,
                            String licenceNumber, String assignedOffice);

    Response updateDoctor(long doctorId, String firstname, String lastname,
                          String password, String confirmPassword,
                          Specialty specialty, String licenceNumber, String assignedOffice);
}
