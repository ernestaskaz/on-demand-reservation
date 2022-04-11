package com.bootcamp.ondemandreservation.service;

import com.bootcamp.ondemandreservation.model.Appointment;
import java.util.List;

public interface AppointmentService {


    Appointment saveAppointment(Long doctorId, Long patientId);
    Appointment saveAppointment(Appointment appointment);
    List<Appointment> getAllAppointments();
    List<Appointment> findAvailableAndNotReserved();
    List<Appointment> getTodaysAppointments();
    Appointment getAppointmentById(Long appointmentId);
    void cancelAppointment(Long appointmentId);
    void deleteAppointment(Long appointmentId);
    boolean flipAppointmentAvailable(Long appointmentId);
    void setAppointmentWasAttended(Long appointmentId);
    void addComment(Long appointmentId, String comment);
    void reserveAppointment(Long patientId, Long appointmentId);
    void generateAppointmentsBySchedule(Long doctorId, int daysCount);
    Appointment updateAppointment(Long appointmentId, Appointment appointment);
    boolean cancelAppointment(Long patientId, Long appointmentId);

    // find by doctor id list, roles? doctor/admin can also see patient id list
    // find by patient id list roles? patient can only see his appointments.


}
