package com.bootcamp.ondemandreservation.restcontroller;

import com.bootcamp.ondemandreservation.model.Admin;
import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Schedule;
import com.bootcamp.ondemandreservation.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize(Admin.ADMIN_ROLE)//more fine-grained later
@RequestMapping("/api/v1/doctor")
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

//    @GetMapping("/appointments/{id}")
//    public List<Appointment> getAllAppointments(@PathVariable("id") Long id) {
//        return doctorService.getAllAppointments(id);
//    }

    @GetMapping("/schedule/{id}")
    public List<Schedule> getDoctorSchedules(@PathVariable("id") Long id) {
        return doctorService.getDoctorSchedules(id);
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
