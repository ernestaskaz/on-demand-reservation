package com.bootcamp.ondemandreservation.serviceimpl;


import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Schedule;
import com.bootcamp.ondemandreservation.repository.DoctorRepository;
import com.bootcamp.ondemandreservation.repository.ScheduleRepository;
import com.bootcamp.ondemandreservation.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceImplementationTest {

    @Mock
    ScheduleRepository scheduleRepository;
    @Mock
    DoctorRepository doctorRepository;

    ScheduleService scheduleService;

    @BeforeEach
    void init() {
        scheduleService = new ScheduleServiceImplementation(scheduleRepository, doctorRepository);

    }


    @Test
    @Order(1)
    void canSaveSchedule() {
        Schedule schedule = new Schedule(1L, DayOfWeek.MONDAY, 12, 19, 15);
        //context

        Mockito.when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Schedule savedSchedule = scheduleService.saveSchedule(schedule);

        assertEquals("schedule day is" + savedSchedule.getDayOfWeek(), schedule.getDayOfWeek(), savedSchedule.getDayOfWeek());


    }

    @Test
    @Order(2)
    void canGetAllSchedules() {
        Schedule schedule = new Schedule(1L, DayOfWeek.MONDAY, 12, 19, 15);
        List<Schedule> scheduleList = new ArrayList<>();
        scheduleList.add(schedule);
        //context

        Mockito.when(scheduleRepository.findAll()).thenReturn(scheduleList);

        List<Schedule> foundSchedules = scheduleService.getAllSchedules();

        assertEquals("something went wrong", 1, foundSchedules.size());

    }

    @Test
    @Order(3)
    void canFindScheduleById() {


        Schedule schedule = new Schedule(1L, DayOfWeek.MONDAY, 12, 19, 15);
        //context

        Mockito.when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

        Schedule savedSchedule = scheduleService.findScheduleById(1L);

        assertEquals("schedule day is" + savedSchedule.getDayOfWeek(), schedule.getDayOfWeek(), savedSchedule.getDayOfWeek());


    }


    @Test
    @Order(4)
    void canSetDoctorToSchedule() {

        Schedule schedule = new Schedule(1L, DayOfWeek.MONDAY, 12, 19, 15);

        Mockito.when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

        Doctor doctor = new Doctor(1L,"this is name", "this is lastName", "Specialty");

        Mockito.when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        scheduleService.setDoctorToSchedule(1L, 1L);


        assertEquals("day of week is" +  doctor.getSchedulesList().get(0).getDayOfWeek(), schedule.getDayOfWeek(), doctor.getSchedulesList().get(0).getDayOfWeek());


    }

    @Test
    @Order(5)
    void canUpdateSchedule() {
        Schedule schedule = new Schedule(1L, DayOfWeek.MONDAY, 12, 19, 15);
        Schedule scheduleNewValues = new Schedule(1L, DayOfWeek.TUESDAY, 15, 23, 17);

        Mockito.when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

        Mockito.when(scheduleRepository.save(any(Schedule.class))).thenReturn(scheduleNewValues);

        Doctor doctor = new Doctor(1L,"this is name", "this is lastName", "Specialty");

        Mockito.when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        scheduleService.setDoctorToSchedule(1L, 1L);
        Schedule updatedSchedule = scheduleService.updateSchedule(1L, scheduleNewValues);



        assertEquals("schedule doctor name is " +  updatedSchedule.getDoctor().getFirstName(), doctor.getFirstName(),updatedSchedule.getDoctor().getFirstName());
        assertEquals("updated schedule day is  " +  updatedSchedule.getDayOfWeek(), scheduleNewValues.getDayOfWeek(),updatedSchedule.getDayOfWeek());


    }


    @Test
    @Order(6)
    void canValidateSchedule() {
        Schedule schedule = new Schedule(1L, DayOfWeek.MONDAY, 12, 19, 15);

        Map<String, String> errors = scheduleService.validateSchedule(schedule);



        assertEquals("number of errors is " +  errors.size(), 0,errors.size());


    }


    @Test
    @Order(7)
    void invalidatesSchedule() {
        Schedule schedule = new Schedule(1L, DayOfWeek.MONDAY, 124, 194, 204);

        Map<String, String> errors = scheduleService.validateSchedule(schedule);



        assertEquals("number of errors is " +  errors.size(), 3,errors.size());


    }

    @Test
    @Order(8)
    void invalidatesScheduleWithValues() {
        Schedule schedule = new Schedule(1L, DayOfWeek.MONDAY, 124, 194, 204);

        Map<String, String> errors = scheduleService.validateSchedule(schedule);



        assertEquals("number of errors is " +  errors.size(), 3,errors.size());
        assertEquals("error for startHour is " +  errors.get("startHour"), "Please provide a start hour(number) between 1 and 23",errors.get("startHour"));
        assertEquals("error for endHour is " +  errors.get("endHour"), "Please provide an end hour(number) between 1 and 23",errors.get("endHour"));
        assertEquals("error for lunchTime is " +  errors.get("lunchTime"), "Please provide a lunch hour that is between Start Hour and End Hour",errors.get("lunchTime"));


    }



}
