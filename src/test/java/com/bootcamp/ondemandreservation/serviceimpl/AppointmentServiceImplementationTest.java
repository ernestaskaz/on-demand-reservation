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
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.bootcamp.ondemandreservation.serviceimpl.AppointmentServiceImplementation.SORT_BY_APPOINTMENT_TIME;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThrows;
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

        Mockito.when(appointmentRepository.findAll(SORT_BY_APPOINTMENT_TIME)).thenReturn(appointmentList);

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

        Mockito.when(appointmentRepository.findByIsAvailableTrueAndIsReservedFalseAndAppointmentTimeIsAfter(LocalDateTime.now(), SORT_BY_APPOINTMENT_TIME)).thenReturn(appointmentList);

        List<Appointment> foundAppointments = appointmentService.findAvailableAndNotReserved();

        assertEquals("size is " + appointmentList.size(), appointmentList.size() ,foundAppointments.size());


    }


    @Test
    @Order(3)
    void canGetTodayUpcomingAppointments() {

        Appointment firstAppointment = new Appointment(LocalDateTime.now().plusHours(1));
        Appointment secondAppointment = new Appointment(LocalDateTime.now().plusDays(1));
        Appointment thirdAppointment = new Appointment(LocalDateTime.now().plusDays(2));


        List<Appointment> appointmentList = new ArrayList<>();
        appointmentList.add(firstAppointment);
        appointmentList.add(secondAppointment);
        appointmentList.add(thirdAppointment);

        Mockito.when(appointmentRepository.findByAppointmentTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class), any(Sort.class))).thenReturn(appointmentList);


        List<Appointment> foundAppointments = appointmentService.getTodaysAppointments();

        assertEquals("should be 3 appointments" + foundAppointments.size(), 3 ,foundAppointments.size());


    }

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

        Mockito.when(appointmentRepository.save(any(Appointment.class))).thenReturn(DateTimeValueToUpdate);

        Appointment updatedAppointment = appointmentService.updateAppointment(1L, DateTimeValueToUpdate);
        // might fail, since it checks LocalDateTime values up to miliseconds.
        assertEquals("appointment time changed" + appointment.getAppointmentTime(), updatedAppointment.getAppointmentTime(),DateTimeValueToUpdate.getAppointmentTime());
        assertEquals("patient is present" , patient.getFirstName(),updatedAppointment.getPatient().getFirstName());
        assertEquals("doctor is present" , doctor.getFirstName(),updatedAppointment.getDoctor().getFirstName());



    }


    @Test
    @Order(7)
    void canGenerateAppointments() {
        Schedule schedule = new Schedule(1L, DayOfWeek.THURSDAY, 8, 19, 15);
        List<Schedule> scheduleList = new ArrayList<>();
        List<Appointment> appointmentList = new ArrayList<>();
        scheduleList.add(schedule);

        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty", scheduleList, appointmentList);

        Mockito.when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        appointmentService.generateAppointmentsBySchedule(1L, 30);



        assertTrue("actual size is " + doctor.getAppointmentList().size(), doctor.getAppointmentList().size() > 5 );

    }

    @Test
    @Order(8)
    void canFlipAppointment() {

        Appointment appointment = new Appointment(LocalDateTime.now());
        boolean originalIsAvailable = appointment.isAvailable();

        Mockito.when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        Mockito.when(appointmentRepository.save(any(Appointment.class))).thenReturn((appointment));

        boolean expected = appointmentService.flipAppointmentAvailable(1L);

        assertEquals("something went wrong", expected, !originalIsAvailable);

    }


    @Test
    @Order(9)
    void canSetAppointmentWasAttended() {

        Appointment appointment = new Appointment(LocalDateTime.now());
        boolean originalIsAvailable = appointment.isWasAttended();

        Mockito.when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        Mockito.when(appointmentRepository.save(any(Appointment.class))).thenReturn((appointment));

        appointmentService.setAppointmentWasAttended(1L);

        assertEquals("something went wrong", !originalIsAvailable, appointment.isWasAttended());

    }

    @Test
    @Order(10)
    void canAddComment() {

        Appointment appointment = new Appointment(LocalDateTime.now());

        Mockito.when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        Mockito.when(appointmentRepository.save(any(Appointment.class))).thenReturn((appointment));

        appointmentService.addComment(1L, "this is comment");

        assertEquals("something went wrong", "this is comment", appointment.getComment());

    }

    @Test
    @Order(11)
    void reserveReservedAppointmentThrowsException() {

        Patient patient = new Patient(1L, "firstName", "lastName");
        Mockito.when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        Doctor doctor = new Doctor(1L,"this is name", "this is lastName", "Specialty");
        //context

        Appointment appointment = new Appointment(LocalDateTime.now(), doctor);

        Mockito.when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        appointmentService.reserveAppointment(1L, 1L);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.reserveAppointment(1L, 1L);;
        });


    }


    @Test
    @Order(12)
    void canGenerateAppointmentsForSecondTime() {
        Schedule schedule = new Schedule(1L, DayOfWeek.THURSDAY, 8, 19, 15);
        List<Schedule> scheduleList = new ArrayList<>();
        List<Appointment> appointmentList = new ArrayList<>();
        scheduleList.add(schedule);

        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty", scheduleList, appointmentList);

        Mockito.when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        appointmentService.generateAppointmentsBySchedule(1L, 30);
        int firstIteration = doctor.getAppointmentList().size();
        appointmentService.generateAppointmentsBySchedule(1L, 30);
        int expected = firstIteration * 2;



//        assertTrue("actual size is " + doctor.getAppointmentList().size(), doctor.getAppointmentList().size() > 5 );
        assertEquals("something went wrong" + doctor.getAppointmentList().size(), expected, doctor.getAppointmentList().size());

    }

    /**
     * Having issues with these tests. Can not think of a way to mock repository method that has specific parameters. Since we do not do any data modification in service layer,
     * maybe it is not testable? The method does return just a list that is modified at the database level.
     */

//    @Test
//    @Order(12)
//    void canGetUpcomingAppointmentsForTodayByDoctorId() {
//
//        //sometimes this test might fail because of the fraction of LocalDateTime seconds that are not truncated in actual method. basically mismatch of arguments by small part of a second
//        Schedule schedule = new Schedule(1L, DayOfWeek.THURSDAY, 8, 19, 15);
//        List<Schedule> scheduleList = new ArrayList<>();
//        List<Appointment> appointmentList = new ArrayList<>();
//        scheduleList.add(schedule);
//
//        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty", scheduleList, appointmentList);
//
//        Mockito.when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
//
//        appointmentService.generateAppointmentsBySchedule(1L, 30);
//
//        Mockito.lenient().when(appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS), SORT_BY_APPOINTMENT_TIME))
//                .thenReturn(appointmentList);
//
//
////        Mockito.lenient().when(appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(anyLong(),any(LocalDateTime.class), any(LocalDateTime.class), any(Sort.class)))
////                .thenReturn(appointmentList);
//
//        List<Appointment> actualList = appointmentService.getUpcomingAppointmentsForTodayByDoctorId(1L);
//
//        for (Appointment appointment: actualList
//        ) {
//            System.out.println(appointment.getAppointmentTime());
//
//        }
//
//        assertTrue("actual  " + actualList.get(0).getAppointmentTime(), actualList.get(0).getAppointmentTime().isAfter(LocalDateTime.now()));
//
//    }
//    @Test
//    @Order(13)
//    void canGetPastAppointmentsByDoctorId() {
//
//        //sometimes this test might fail because of the fraction of LocalDateTime seconds that are not truncated in actual method. basically mismatch of arguments by small part of a second
//        Schedule schedule = new Schedule(1L, DayOfWeek.THURSDAY, 12, 19, 15);
//        List<Schedule> scheduleList = new ArrayList<>();
//        List<Appointment> appointmentList = new ArrayList<>();
//        scheduleList.add(schedule);
//
//        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty", scheduleList, appointmentList);
//
//        Mockito.when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
//
//        appointmentService.generateAppointmentsBySchedule(1L, 30);
//
//        LocalDateTime start = LocalDateTime.of(2000, Month.JULY, 1, 00, 00, 00);
//
//        Mockito.lenient().when(appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(1L, start, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), SORT_BY_APPOINTMENT_TIME))
//                .thenReturn(appointmentList);
//
//        List<Appointment> actualList = appointmentService.getPastAppointmentsByDoctorId(1L);
//
//        for (Appointment appointment: actualList
//             ) {
//            System.out.println(appointment);
//
//        }
//
////        assertTrue("actual size is " + actualList.get(0), actualList.get(0).getAppointmentTime().isAfter(LocalDateTime.now()));
//
//        assertTrue("actual size is " + actualList.size(), doctor.getAppointmentList().size() > 55 );
//
//    }


}
