package com.bootcamp.ondemandreservation.Controller;

import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Model.Patient;
import com.bootcamp.ondemandreservation.Service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<Doctor> saveDoctor(@RequestBody Doctor doctor) {
        return new ResponseEntity<Doctor>(doctorService.saveDoctor(doctor), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/appointments/{id}")
    public List<Appointment> getAllAppointments(@PathVariable("id") Long id) {
        return doctorService.getAllAppointments(id);
    }

    @GetMapping ("{id}")
    public ResponseEntity<Doctor> findDoctorById(@PathVariable("id") Long id) {
        return new ResponseEntity<Doctor>(doctorService.findDoctorById(id), HttpStatus.OK);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteDoctorById(@PathVariable("id") Long id) {
        doctorService.deleteDoctor(id);
        return new ResponseEntity<String>("Doctor successfully deleted", HttpStatus.OK);
    }

    @PutMapping ("{id}")
    public Doctor updateDoctor(@PathVariable("id") Long id, @RequestBody Doctor doctor) {
        return doctorService.updateDoctor(id, doctor);

    }



}
