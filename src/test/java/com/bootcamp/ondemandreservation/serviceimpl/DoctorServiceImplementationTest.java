package com.bootcamp.ondemandreservation.serviceimpl;


import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Schedule;
import com.bootcamp.ondemandreservation.repository.DoctorRepository;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.service.AppointmentService;
import com.bootcamp.ondemandreservation.service.DoctorService;
import com.bootcamp.ondemandreservation.service.ODRUserService;
import com.bootcamp.ondemandreservation.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceImplementationTest {

    @Mock
    DoctorRepository doctorRepository;
    @Mock
    ODRPasswordEncoder odrPasswordEncoder;
    @Mock
    ODRUserService odrUserService;
    @Mock
    ScheduleService scheduleService;
    @Mock
    AppointmentService appointmentService;

    DoctorService doctorService;

    @BeforeEach
    void init() {
        doctorService = new DoctorServiceImplementation(doctorRepository, odrPasswordEncoder, odrUserService, scheduleService, appointmentService);
    }

    @Test
    @Order(1)
    void canSaveDoctor() {
        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty");
        //context

        Mockito.when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor savedDoctor = doctorService.saveDoctor(doctor);
        //Doctor savedDoctor = doctorRepository.save(doctor);

        assertEquals("something went wrong", doctor.getFirstName(), savedDoctor.getFirstName());


    }

    @Test
    @Order(2)
    void canGetAllDoctors() {
        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty");
        List<Doctor> doctorList = new ArrayList<>();
        doctorList.add(doctor);
        //context

        Mockito.when(doctorRepository.findAll()).thenReturn(doctorList);

        List<Doctor> doctorsToTest = doctorService.getAllDoctors();

        assertEquals("something went wrong", 1, doctorsToTest.size());

    }

    @Test
    @Order(3)
    void canFindDoctorById() {
        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty");
        //context
        Mockito.when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        Doctor foundDoctor = doctorService.findDoctorById(1L);

        assertEquals("something went wrong", doctor.getFirstName(), foundDoctor.getFirstName());


    }


    @Test
    @Order(4)
    void canGetDoctorAppointments() {
        List<Appointment> appointmentList = new ArrayList<>();
        List<Schedule> scheduleList = new ArrayList<>();
        Appointment appointment = new Appointment();
        Schedule schedule = new Schedule();
        appointmentList.add(appointment);
        scheduleList.add(schedule);
        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty", scheduleList, appointmentList);

        Mockito.when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        Doctor foundDoctor = doctorService.findDoctorById(1L);


        assertEquals("size is " + foundDoctor.getAppointmentList().size(), doctor.getAppointmentList().size(), foundDoctor.getAppointmentList().size());


    }

    @Test
    @Order(5)
    void canGetDoctorSchedules() {
        List<Appointment> appointmentList = new ArrayList<>();
        List<Schedule> scheduleList = new ArrayList<>();
        Appointment appointment = new Appointment();
        Schedule schedule = new Schedule();
        appointmentList.add(appointment);
        scheduleList.add(schedule);
        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty", scheduleList, appointmentList);

        Mockito.when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        Doctor foundDoctor = doctorService.findDoctorById(1L);


        assertEquals("size is " + foundDoctor.getSchedulesList().size(), doctor.getSchedulesList().size(), foundDoctor.getSchedulesList().size());


    }

    @Test
    @Order(6)
    void canUpdateDoctor() {

        Doctor doctor = new Doctor("this is name", "this is changed lastName", "Specialty");


        Mockito.when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor updatedDoctor = doctorService.updateDoctor(1L, doctor);


        assertEquals("something went wrong", doctor.getLastName(), updatedDoctor.getLastName());


    }

//    @Test
//    @Order(7)
//    void canGetUpcomingAppointmentsForToday() {
//
//        List<Appointment> appointmentList = new ArrayList<>();
//        List<Schedule> scheduleList = new ArrayList<>();
//        Appointment appointment = new Appointment(LocalDateTime.now().plusHours(1));
//        Appointment appointmentTwo = new Appointment(LocalDateTime.now().plusDays(1));
//        Schedule schedule = new Schedule();
//        appointmentList.add(appointment);
//        appointmentList.add(appointmentTwo);
//        scheduleList.add(schedule);
//        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty", scheduleList, appointmentList);
//
//        Mockito.when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
//
//        List<Appointment> foundAppointments = doctorService.getUpcomingAppointmentsForToday(1L);
//
//
//        assertEquals("size is " + foundAppointments.size(), 1, foundAppointments.size());
//
//
//    }



}
