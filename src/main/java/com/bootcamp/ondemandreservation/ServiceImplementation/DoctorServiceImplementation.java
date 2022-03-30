package com.bootcamp.ondemandreservation.ServiceImplementation;

import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Repository.DoctorRepository;
import com.bootcamp.ondemandreservation.Service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DoctorServiceImplementation implements DoctorService {

    private DoctorRepository doctorRepository;


    public DoctorServiceImplementation(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor findDoctorById(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return doctor.get();
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public List<Appointment> getAllAppointments(Long id) {
        Doctor doctor = findDoctorById(id);
        return doctor.getAppointmentList();
    }
}
