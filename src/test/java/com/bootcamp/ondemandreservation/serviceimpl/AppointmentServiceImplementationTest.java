package com.bootcamp.ondemandreservation.serviceimpl;

import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.model.Schedule;
import com.bootcamp.ondemandreservation.repository.AppointmentRepository;
import com.bootcamp.ondemandreservation.repository.DoctorRepository;
import com.bootcamp.ondemandreservation.repository.PatientRepository;
import com.bootcamp.ondemandreservation.repository.ScheduleRepository;
import com.bootcamp.ondemandreservation.service.AppointmentService;
import org.checkerframework.checker.units.qual.A;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplementationTest {

    @Mock
    DoctorRepository doctorRepository;
    @Mock
    AppointmentRepository appointmentRepository;
    @Mock
    PatientRepository patientRepository;

    AppointmentService appointmentService;

    @BeforeEach
    void init() {
        appointmentService = new AppointmentServiceImplementation(appointmentRepository, doctorRepository, patientRepository,null);

    }

    @Test
    @Order(1)
    void canSaveAppointment() {
        //context
        Patient patient = new Patient(1L, "firstName", "lastName");
        Mockito.when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        Doctor doctor = new Doctor(1L,"this is name", "this is lastName", "Specialty");

        Mockito.when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        Appointment savedAppointment =  appointmentService.saveAppointment(1L, 1L);

        assertEquals("something went wrong", 1, doctor.getAppointmentList().size());
        assertEquals("something went wrong", 1, patient.getAppointmentList().size());
        assertEquals("something went wrong", doctor.getFirstName(),savedAppointment.getDoctor().getFirstName());


    }

    @Test
    @Order(2)
    void canGetAllAppointments() {

        Appointment appointment = new Appointment();
        List<Appointment> appointmentList = new ArrayList<>();
        appointmentList.add(appointment);

        Mockito.when(appointmentRepository.findAll()).thenReturn(appointmentList);

        List<Appointment> foundAppointments = appointmentService.getAllAppointments();

        assertEquals("size is " + foundAppointments.size(), appointmentList.size() ,foundAppointments.size());

    }
//test
    @Test
    @Order(3)
    void canFindAvailableAndNotReservedAppointments() {

        Patient patient = new Patient(1L, "firstName", "lastName");
        Mockito.when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        Doctor doctor = new Doctor(1L,"this is name", "this is lastName", "Specialty");
        //context
        Mockito.when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        Appointment savedAppointment =  appointmentService.saveAppointment(1L, 1L);

        List<Appointment> appointmentList = new ArrayList<>();
        appointmentList.add(savedAppointment);

        Mockito.when(appointmentRepository.findByIsAvailableTrueAndIsReservedFalseAndAppointmentTimeIsAfter(LocalDateTime.now())).thenReturn(appointmentList);

        List<Appointment> foundAppointments = appointmentService.findAvailableAndNotReserved();

        assertEquals("size is " + appointmentList.size(), appointmentList.size() ,foundAppointments.size());


    }


//    @Test
//    @Order(3)
//    void canGetTodayUpcomingAppointments() {
//
//        Appointment firstAppointment = new Appointment(LocalDateTime.now().plusHours(1));
//        Appointment secondAppointment = new Appointment(LocalDateTime.now().plusDays(1));
//        Appointment thirAppointment = new Appointment(LocalDateTime.now().plusDays(2));
//
//
//        List<Appointment> appointmentList = new ArrayList<>();
//        appointmentList.add(firstAppointment);
//        appointmentList.add(secondAppointment);
//        appointmentList.add(thirAppointment);
//
//        Mockito.when(appointmentRepository.findAll()).thenReturn(appointmentList);
//
//        List<Appointment> foundAppointments = appointmentService.getTodaysAppointments();
//
//        assertEquals("since repo list returns 3, only 1 should be today" + foundAppointments.size(), 1 ,foundAppointments.size());
//
//
//    }

    @Test
    @Order(4)
    void canFindAppointmentById() {

        Appointment appointment = new Appointment(LocalDateTime.now());

        Mockito.when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Appointment foundAppointment = appointmentService.getAppointmentById(1L);


        assertEquals("something went wrong", appointment.getAppointmentTime(), foundAppointment.getAppointmentTime());
    }

    @Test
    @Order(4)
    void canCancelAppointment() {

        Appointment appointment = new Appointment(LocalDateTime.now(), true, false);

        Mockito.when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        appointmentService.cancelAppointment(1L);

        assertEquals("something went wrong", true, appointment.isAvailable());
    }


    @Test
    @Order(5)
    void canReserveAppointment() {

        Patient patient = new Patient(1L, "firstName", "lastName");
        Mockito.when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        Doctor doctor = new Doctor(1L,"this is name", "this is lastName", "Specialty");
        //context

        Appointment appointment = new Appointment(LocalDateTime.now(), doctor);

        Mockito.when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        appointmentService.reserveAppointment(1L, 1L);



        assertEquals("name is" + appointment.getPatient().getFirstName(), patient.getFirstName() , appointment.getPatient().getFirstName());
        assertEquals("reserved status" + appointment.isReserved(), true, appointment.isReserved());
        assertEquals("available status" + appointment.isAvailable(), false, appointment.isAvailable());


    }

    @Test
    @Order(6)
    void canUpdateAppointment() {


        Patient patient = new Patient(1L, "firstName", "lastName");

        Doctor doctor = new Doctor(1L,"this is name", "this is lastName", "Specialty");

        Appointment appointment =  new Appointment(doctor, patient);

        Mockito.when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Appointment DateTimeValueToUpdate = new Appointment(LocalDateTime.now().plusDays(1));

        appointmentService.updateAppointment(1L, DateTimeValueToUpdate);

        assertEquals("patient is present" , patient.getFirstName(),DateTimeValueToUpdate.getPatient().getFirstName());
        assertEquals("doctor is present" , doctor.getFirstName(),DateTimeValueToUpdate.getDoctor().getFirstName());



    }


    @Test
    @Order(7)
    void canGenerateAppointments() {
        Schedule schedule = new Schedule(1L, DayOfWeek.THURSDAY, 12, 19, 15);
        List<Schedule> scheduleList = new ArrayList<>();
        List<Appointment> appointmentList = new ArrayList<>();
        scheduleList.add(schedule);

        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty", scheduleList, appointmentList);

        Mockito.when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        appointmentService.generateAppointmentsBySchedule(1L, 30);

        assertTrue("actual size is " + doctor.getAppointmentList().size(), doctor.getAppointmentList().size() > 5 );

    }




}
