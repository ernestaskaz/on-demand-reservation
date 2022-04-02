package com.bootcamp.ondemandreservation.Controller;

import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Model.Schedule;
import com.bootcamp.ondemandreservation.Service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private ScheduleService scheduleService;


    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<Schedule> saveSchedule(@RequestBody Schedule schedule) {
        return new ResponseEntity<Schedule>(scheduleService.saveSchedule(schedule), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @PutMapping("/{scheduleId}/{doctorId}")
    ResponseEntity<String> setDoctorToSchedule(@PathVariable("scheduleId") Long scheduleId, @PathVariable("doctorId") Long doctorId) {
        scheduleService.setDoctorToSchedule(scheduleId, doctorId);
        return new ResponseEntity<String>("Schedule has been added to selected doctor", HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteScheduleById(@PathVariable("id") Long id) {
        scheduleService.deleteScheduleById(id);
        return new ResponseEntity<String>("Schedule successfully deleted", HttpStatus.OK);
    }

    @PutMapping ("{id}")
    public Schedule updateSchedule(@PathVariable("id") Long id, @RequestBody Schedule schedule) {
        return scheduleService.updateSchedule(id, schedule);

    }


}
