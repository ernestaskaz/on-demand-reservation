package com.bootcamp.ondemandreservation.serviceimpl;


import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.repository.DoctorRepository;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.service.DoctorService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

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

    DoctorService doctorService;

    @BeforeEach
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

//comment
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



}
