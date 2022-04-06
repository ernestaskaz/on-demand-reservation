package com.bootcamp.ondemandreservation.serviceimpl;

import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.ODRUser;
import com.bootcamp.ondemandreservation.model.ODRUserNotFoundException;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.repository.PatientRepository;
import com.bootcamp.ondemandreservation.security.ODRInputSanitiser;
import com.bootcamp.ondemandreservation.service.ODRUserService;
import com.bootcamp.ondemandreservation.service.PatientService;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PatientServiceImplementation implements PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ODRPasswordEncoder odrPasswordEncoder;
    @Autowired
    private ODRUserService odrUserService;


    public PatientServiceImplementation() {
    }

    public PatientServiceImplementation(PatientRepository patientRepository, ODRPasswordEncoder odrPasswordEncoder, ODRUserService odrUserService) {
        this.patientRepository = patientRepository;
        this.odrPasswordEncoder = odrPasswordEncoder;
        this.odrUserService = odrUserService;
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
    @Override
    public Patient getLoggedInPatient() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id=null;
        if (principal instanceof ODRUser) {
            id = ((ODRUser)principal).getId();
        } else {
            throw new ODRUserNotFoundException();
        }
        Patient  patient=findPatientById(id);
        if(patient==null){
            throw new ODRUserNotFoundException();
        }
        return patient;
    }

    @Override
    public Map<String, String> validatePatient(Patient patient) {
        Map<String, String> rv = new HashMap<>(odrUserService.validate(patient));
        if(!patient.getPhoneNumber().isBlank()&&!ODRInputSanitiser.seemsToBePhoneNumber( patient.getPhoneNumber())){
            rv.put("phoneNumber","invalid");
        }


        return rv;
    }
}
