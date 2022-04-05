package com.bootcamp.ondemandreservation.ServiceImplementation;

import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Model.Patient;
import com.bootcamp.ondemandreservation.Repository.PatientRepository;
import com.bootcamp.ondemandreservation.Service.PatientService;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImplementation implements PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ODRPasswordEncoder odrPasswordEncoder;


    public PatientServiceImplementation() {
    }

    public PatientServiceImplementation(PatientRepository patientRepository, ODRPasswordEncoder odrPasswordEncoder) {
        this.patientRepository = patientRepository;
        this.odrPasswordEncoder = odrPasswordEncoder;
    }

    @Override
    public void changePassword(Long id, String plaintextPassword) {
        Patient thePatient=findPatientById(id);
        thePatient.setPassword(odrPasswordEncoder.defaultPasswordEncoder().encode(plaintextPassword));
        savePatient(thePatient);


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

    /**
     * @param id passed from Controller to find specific patient.
     *  To delete a Patient object, doctor can not have any relations to other tables.
     *  This method removes relations to any Appointment list that a patient might have relations to.
     *  Deletes patient.
     */

    @Override
    public void deletePatientById(Long id) {
        Patient currentPatient  = findPatientById(id);
        currentPatient.removePatientFromAppointmentList();
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
