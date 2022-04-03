package com.bootcamp.ondemandreservation.Controller;

import com.bootcamp.ondemandreservation.Helpers;
import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Model.Patient;
import com.bootcamp.ondemandreservation.Service.DoctorService;
import org.junit.BeforeClass;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DoctorControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @Test
    @Order(1)
    void canSaveDoctor() throws Exception {

        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty");

        mockMvc.perform(MockMvcRequestBuilders.post("/doctor")
                        .content(Helpers.asJsonString(doctor))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.specialty").value("Specialty"));
    }

    @Test
    @Order(3)
    void canFindDoctorById() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/1")
                        .accept(MediaType.ALL_VALUE))
                .andDo(print())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.specialty").value("Specialty"));
    }

    @Test
    @Order(2)
    void canGetAllDoctors () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor")
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].specialty").value("Specialty"));

    }

    @Test
    @Order(4)
    void canUpdateDoctor () throws Exception {

        Doctor newDoctor = new Doctor("this is name", "this is lastName", "SpecialtyChanged");
        mockMvc.perform(MockMvcRequestBuilders.put("/doctor/1")
                        .content(Helpers.asJsonString(newDoctor))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.specialty").value("SpecialtyChanged"));

    }

//    @Test
//    @Order(5)
//    void canGetAppointments () throws Exception {
//        Patient patient = new Patient(1L, "firstName", "lastName");
//        Doctor doctor = new Doctor(1L, "this is name", "this is lastName", "Specialty");
//        Appointment appointment = new Appointment(doctor, patient);
//        doctor.addAppointmentList(appointment);
//        mockMvc.perform(MockMvcRequestBuilders.put("/appointments/1")
//                        .content(Helpers.asJsonString(doctor))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(jsonPath("$.specialty").value("Specialty"));
//
//    }

    @Test
    @Order(6)
    void canDeleteDoctorById () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/doctor/1")
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(org.hamcrest.Matchers.equalTo("Doctor successfully deleted")));

    }


}
