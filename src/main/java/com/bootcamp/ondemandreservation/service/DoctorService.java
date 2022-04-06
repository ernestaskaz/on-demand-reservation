package com.bootcamp.ondemandreservation.service;

import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Schedule;

import java.util.List;

public interface DoctorService {

    void changePassword(Long id, String plaintextPassword);
    Doctor saveDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
    Doctor findDoctorById(Long id);
    void deleteDoctor(Long id);
    List<Appointment> getAllAppointments(Long id);
    List<Schedule> getDoctorSchedules(Long id);
    Doctor updateDoctor(Long id, Doctor doctor);
    List<Appointment> getTodaysAppointments(Long id);

}
