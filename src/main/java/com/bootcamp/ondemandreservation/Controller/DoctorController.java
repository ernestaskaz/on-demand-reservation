package com.bootcamp.ondemandreservation.Controller;

import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping ("{id}")
    public Doctor findDoctorById(@PathVariable("id") Long id) {
        return doctorService.findDoctorById(id);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteDoctorById(@PathVariable("id") Long id) {
        doctorService.deleteDoctor(id);
        return new ResponseEntity<String>("Doctor successfully deleted", HttpStatus.OK);
    }



}
