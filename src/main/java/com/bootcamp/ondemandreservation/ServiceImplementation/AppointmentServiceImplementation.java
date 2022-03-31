package com.bootcamp.ondemandreservation.ServiceImplementation;

import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Model.Patient;
import com.bootcamp.ondemandreservation.Repository.AppointmentRepository;
import com.bootcamp.ondemandreservation.Repository.DoctorRepository;
import com.bootcamp.ondemandreservation.Repository.PatientRepository;
import com.bootcamp.ondemandreservation.Service.AppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImplementation implements AppointmentService {

    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;

    public AppointmentServiceImplementation(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public Appointment saveAppointment(Long doctorId, Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId).get();
        Patient patient = patientRepository.findById(patientId).get();

        Appointment appointment = new Appointment(doctor, patient);

        appointment.setAvailable(false);
        appointment.setReserved(true);

        doctor.addAppointmentList(appointment);
        patient.addAppointmentList(appointment);
        appointmentRepository.save(appointment);


        return appointment;


    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment getAppointmentById(Long appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        return appointment.get();
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).get();
        appointment.removePatient();
        appointment.setReserved(false);
        appointment.setAvailable(true);
        appointmentRepository.save(appointment);

    }

    @Override
    public void deleteAppointment(Long appointmentId) {
        appointmentRepository.deleteById(appointmentId);

    }
}
