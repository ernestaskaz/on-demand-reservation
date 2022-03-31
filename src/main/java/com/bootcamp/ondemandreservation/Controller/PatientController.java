package com.bootcamp.ondemandreservation.Controller;

import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Model.Patient;
import com.bootcamp.ondemandreservation.Service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<Patient> savePatient(@RequestBody Patient patient) {
        return new ResponseEntity<Patient>(patientService.savePatient(patient), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping ("{id}")
    public Patient findPatientById(@PathVariable("id") Long id) {
        return patientService.findPatientById(id);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePatientById(@PathVariable("id") Long id) {
        patientService.deletePatientById(id);
        return new ResponseEntity<String>("Patient successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/appointments/{id}")
    public List<Appointment> getAllAppointments(@PathVariable("id") Long id) {
        return patientService.getAllAppointments(id);
    }

    @PutMapping ("{id}")
    public Patient updatePatient(@PathVariable("id") Long id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);

    }



}
