package com.bootcamp.ondemandreservation.Controller;

import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//test comment

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    private AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/{doctorId}/{patientId}")
    public ResponseEntity <Appointment> saveAppointment(@PathVariable("doctorId") Long doctorId, @PathVariable("patientId") Long patientId) {
        return new ResponseEntity<Appointment>(appointmentService.saveAppointment(doctorId, patientId), HttpStatus.CREATED);
}

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable("appointmentId") Long appointmentId) {
       return  new ResponseEntity<Appointment>(appointmentService.getAppointmentById(appointmentId), HttpStatus.FOUND);
    }

    @PutMapping("/cancel/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return new ResponseEntity<String>("Appointment has been canceled", HttpStatus.ACCEPTED);
    }
    //does not cascade - contrained entities are not affected.
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<String> deleteAppointment(@PathVariable("appointmentId") Long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
        return new ResponseEntity<String>("The appointment has been deleted", HttpStatus.OK);
    }



//TODO. USE IT LATER WHEN YOU NEED TO AUTO GENERATE APPOINTMENTS WITH NO CONSTRAINS. CHANGE PATH!
//    @PostMapping("/test")
//    public ResponseEntity <Appointment> testAppointment(@RequestBody Appointment appointment) {
//        return new ResponseEntity<Appointment>(appointmentService.saveAppointment(appointment), HttpStatus.CREATED);
//    }





}
