package com.bootcamp.ondemandreservation.service;

import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Schedule;

import java.util.List;
import java.util.Map;

public interface DoctorService {

    void changePassword(Long id, String plaintextPassword);
    Doctor saveDoctor(Doctor doctor, boolean createSchedules);
    Doctor saveDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
    Doctor findDoctorById(Long id);
    void deleteDoctor(Long id);
    //List<Appointment> getAllAppointments(Long id);
    List<Schedule> getDoctorSchedules(Long id);
    Doctor updateDoctor(Long id, Doctor doctor);
    Doctor getLoggedInDoctor();
    /**
     * Validates the doctor
     * @param doctor Doctor to be validated
     * @param matchPassword if we should check for password and confirmPassword to match.
     * @return Map with fields (email, not getEmail) as keys and error messages as values
     */
    Map<String, String> validateDoctor(Doctor doctor, boolean matchPassword, boolean forUpdate);


    Doctor saveDoctorAndPassword(Doctor doctor, boolean createSchedules);
}
