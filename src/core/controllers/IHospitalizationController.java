package core.controllers;

import core.models.Response;
import core.models.RoomType;

/**
 * Interface for hospitalization controller (SOLID — Interface Segregation Principle).
 * Views and other controllers depend on this abstraction, not the concrete class.
 */
public interface IHospitalizationController {

    Response requestHospitalization(long patientId, long doctorId,
                                    String dateStr, String reason,
                                    RoomType roomType, String observations);

    Response acceptHospitalization(String hospId);

    Response cancelHospitalization(String hospId);

    Response getDoctorHospitalizationsResponse(long doctorId);

    /**
     * Converts a COMPLETED appointment into a new ONGOING hospitalization.
     *
     * @param appointmentId the completed appointment to convert
     * @param dateStr       admission date "YYYY-MM-DD"
     * @param reason        reason for hospitalization
     * @param roomType      requested room type
     * @param observations  optional clinical notes
     * @return Response(CREATED) on success; Response(NOT_FOUND/CONFLICT/BAD_REQUEST) on failure.
     */
    Response fromAppointment(String appointmentId, String dateStr,
                              String reason, RoomType roomType, String observations);
}
