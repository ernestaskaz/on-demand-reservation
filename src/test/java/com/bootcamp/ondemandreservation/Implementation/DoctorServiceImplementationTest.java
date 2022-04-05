package com.bootcamp.ondemandreservation.Implementation;

import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.repository.DoctorRepository;
import com.bootcamp.ondemandreservation.service.DoctorService;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.serviceimpl.DoctorServiceImplementation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.junit.jupiter.api.Assertions;
import static junit.framework.TestCase.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceImplementationTest {

    @Mock
    DoctorRepository doctorRepository;
    @Mock
    ODRPasswordEncoder odrPasswordEncoder;

    DoctorService doctorService;
    @BeforeClass
    void init() {
        doctorService = new DoctorServiceImplementation(doctorRepository, odrPasswordEncoder);


    }

    @Test
    @Order(1)
    void canSaveDoctor() {
        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty");
        //context

        Mockito.when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor savedDoctor = doctorService.saveDoctor(doctor);

        assertEquals("something went wrong", doctor.getFirstName(), savedDoctor.getFirstName());


    }

    @Test
    @WithMockUser(username="admin@default.com")
    @Order(1)
    void canGetAllDoctors() {
        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty");
        //context

        Mockito.when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor savedDoctor = doctorService.saveDoctor(doctor);

        assertEquals("something went wrong", doctor.getFirstName(), savedDoctor.getFirstName());


    }
}
