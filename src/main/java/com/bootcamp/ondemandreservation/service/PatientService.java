package com.bootcamp.ondemandreservation.service;

import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Patient;

import java.util.List;
import java.util.Map;

public interface PatientService {

    void changePassword(Long id, String plaintextPassword);
    Patient savePatient(Patient patient);
    List<Patient> getAllPatients();
    Patient findPatientById(Long id);
    void deletePatientById(Long id);
    List<Appointment> getAllAppointments(Long id);
    Patient updatePatient(Long id, Patient patient);
    Patient getLoggedInPatient();

    /**
     * Validates the patient
     * @param patient patient to be validated
     * @return Map with fields (email, not getEmail) as keys and error messages as values
     */
    Map<String,String> validatePatient(Patient patient);
}
