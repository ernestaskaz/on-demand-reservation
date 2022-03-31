package com.bootcamp.ondemandreservation.Service;

import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Doctor;

import java.util.List;

public interface AppointmentService {


    Appointment saveAppointment(Long doctorId, Long patientId);
    Appointment saveAppointment(Appointment appointment);
    List<Appointment> getAllAppointments();
    Appointment getAppointmentById(Long appointmentId);
    void cancelAppointment(Long appointmentId);
    void deleteAppointment(Long appointmentId);

    // find by doctor id list, roles? doctor/admin can also see patient id list
    // find by patient id list roles? patient can only see his appointments.


}
