package com.bootcamp.ondemandreservation.restcontroller;

import com.bootcamp.ondemandreservation.model.Admin;
import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient")
public class PatientController {

    private PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    @PreAuthorize(Admin.ADMIN_ROLE)//more fine-grained later
    @PostMapping
    public ResponseEntity<Patient> savePatient(@RequestBody Patient patient) {
        return new ResponseEntity<Patient>(patientService.savePatient(patient), HttpStatus.CREATED);
    }
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }
    @PreAuthorize(Admin.ADMIN_ROLE)//more fine-grained later
    @GetMapping ("{id}")
    public Patient findPatientById(@PathVariable("id") Long id) {
        return patientService.findPatientById(id);

    }
    @PreAuthorize(Admin.ADMIN_ROLE)//more fine-grained later
    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePatientById(@PathVariable("id") Long id) {
        patientService.deletePatientById(id);
        return new ResponseEntity<String>("Patient successfully deleted", HttpStatus.OK);
    }
    @PreAuthorize(Admin.ADMIN_ROLE)//more fine-grained later
    @GetMapping("/appointments/{id}")
    public List<Appointment> getAllAppointments(@PathVariable("id") Long id) {
        return patientService.getAllAppointments(id);
    }
    @PreAuthorize(Admin.ADMIN_ROLE)//more fine-grained later
    @PutMapping ("{id}")
    public Patient updatePatient(@PathVariable("id") Long id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);

    }
    @PreAuthorize(Admin.ADMIN_ROLE)//more fine-grained later
    @PutMapping ("/password/{id}")
    public void updatePatient(@PathVariable("id") Long id, @RequestBody String password) {
        patientService.changePassword(id,password);

    }


}
