package com.bootcamp.ondemandreservation.Service;

import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Model.Patient;

import java.util.List;

public interface PatientService {

    Patient savePatient(Patient patient);
    List<Patient> getAllPatients();
    Patient findPatientById(Long id);
    void deletePatientById(Long id);
}
