package com.bootcamp.ondemandreservation.serviceimpl;

import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.model.Schedule;
import com.bootcamp.ondemandreservation.repository.PatientRepository;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.service.ODRUserService;
import com.bootcamp.ondemandreservation.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class PatientServiceImplementationTest {

    @Mock
    PatientRepository patientRepository;

    @Mock
    ODRPasswordEncoder odrPasswordEncoder;

    @Mock
    ODRUserService odrUserService;

    PatientService patientService;

    @BeforeEach
    void init() {
        patientService = new PatientServiceImplementation(patientRepository, odrPasswordEncoder, odrUserService);

    }

    @Test
    @Order(1)
    void canSavePatient() {
        Patient patient = new Patient(1L, "firstName", "lastName");
        //context

        Mockito.when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient savedPatient = patientService.savePatient(patient);

        assertEquals("patient name is " + savedPatient.getFirstName(), patient.getFirstName(), savedPatient.getFirstName());

    }

    @Test
    @Order(2)
    void canGetAllPatients() {

        Patient patient = new Patient(1L, "firstName", "lastName");
        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient);

        Mockito.when(patientRepository.findAll()).thenReturn(patientList);

        List<Patient> listToTest = patientService.getAllPatients();


        assertEquals("list size is " + patientList,patientList.size(), listToTest.size());

    }

    @Test
    @Order(3)
    void canFindPatientById() {
        Patient patient = new Patient(1L, "firstName", "lastName");
        //context
        Mockito.when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        Patient foundPatient = patientService.findPatientById(1L);

        assertEquals("name is " + foundPatient.getFirstName(), patient.getFirstName(), foundPatient.getFirstName());


    }

    @Test
    @Order(4)
    void canGetDoctorAppointments() {
        List<Appointment> appointmentList = new ArrayList<>();
        Appointment appointment = new Appointment();
        appointmentList.add(appointment);
        Patient patient = new Patient(1L, "firstName", "lastName", appointmentList);

        Mockito.when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        Patient foundPatient = patientService.findPatientById(1L);


        assertEquals("size is " + foundPatient.getAppointmentList().size(), patient.getAppointmentList().size(), foundPatient.getAppointmentList().size());


    }

    @Test
    @Order(5)
    void canUpdatePatient() {

        Patient patient = new Patient(1L, "changedName", "lastName");


        Mockito.when(patientRepository.save(any(Patient.class))).thenReturn(patient);


        Patient updatedPatient = patientService.updatePatient(1L, patient);

        assertEquals("patient name is " + updatedPatient.getFirstName(), patient.getFirstName(), updatedPatient.getFirstName());


    }



}
