package com.bootcamp.ondemandreservation.ServiceImplementation;

import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Model.Patient;
import com.bootcamp.ondemandreservation.Repository.PatientRepository;
import com.bootcamp.ondemandreservation.Service.PatientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImplementation implements PatientService {

    private PatientRepository patientRepository;

    public PatientServiceImplementation(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient findPatientById(Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.get();
    }

    @Override
    public void deletePatientById(Long id) {
        Patient patient  = findPatientById(id);
        List<Appointment> currentList = patient.getAppointmentList();
        for (Appointment appointment: currentList) {
            appointment.removePatient();
        }
        patientRepository.deleteById(id);
    }

    @Override
    public List<Appointment> getAllAppointments(Long id) {
        Patient patient = findPatientById(id);
        return patient.getAppointmentList();
    }

    @Override
    public Patient updatePatient(Long id, Patient patient) {
        patient.setId(id);
        return savePatient(patient);
    }
}
