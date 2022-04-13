package com.bootcamp.ondemandreservation.restcontroller;

import com.bootcamp.ondemandreservation.model.Admin;
import com.bootcamp.ondemandreservation.model.Schedule;
import com.bootcamp.ondemandreservation.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize(Admin.ADMIN_ROLE)//more fine-grained later
@RequestMapping("/api/v1/schedule")
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
