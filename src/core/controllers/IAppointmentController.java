package core.controllers;

import core.models.Response;
import core.models.Specialty;

/**
 * Interface for appointment controller (SOLID - Interface Segregation Principle).
 */
public interface IAppointmentController {
    Response createAppointment(long patientId, long doctorId, Specialty specialty,
                               String dateStr, String timeStr, String reason, boolean isInPerson);
    Response acceptAppointment(String appointmentId);
    Response completeAppointment(String appointmentId, String diagnosis,
                                 String observations, String treatment, String followUp);
    Response cancelAppointment(String appointmentId);
    Response rescheduleAppointment(String appointmentId, String newDateStr, String newTimeStr, String reason);
    Response prescribe(String appointmentId, String medicationName, String doseStr,
                       String route, String durationStr, String instructions, String frequencyStr);
    Response getAppointmentResponse(String appointmentId);
    Response getPatientAppointmentsResponse(long patientId);
    Response getDoctorAppointmentsResponse(long doctorId, boolean pendingOnly);
}
