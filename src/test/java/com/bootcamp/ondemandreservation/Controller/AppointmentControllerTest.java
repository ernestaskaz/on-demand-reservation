package com.bootcamp.ondemandreservation.Controller;

import com.bootcamp.ondemandreservation.Helpers;
import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Model.Patient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @Order(1)
    void canSaveAppointment() throws Exception {

        Patient patient = new Patient(1L, "firstName", "lastName");

        mockMvc.perform(MockMvcRequestBuilders.post("/patient")
                        .content(Helpers.asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("firstName"));

        //create Doctor;
        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty");

        mockMvc.perform(MockMvcRequestBuilders.post("/doctor")
                        .content(Helpers.asJsonString(doctor))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.specialty").value("Specialty"));

        //create and map appointment;

        mockMvc.perform(MockMvcRequestBuilders.post("/appointment/3/2")
                        .content(Helpers.asJsonString(doctor))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patient.id").value(2L))
                .andExpect(jsonPath("$.doctor.firstName").value("this is name"));
    }


    @Test
    @Order(2)
    void canGetAllAppointments () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/appointment")
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].id").value(1L));

    }

    @Test
    @Order(3)
    void canFindAppointmentById() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/appointment/1")
                        .accept(MediaType.ALL_VALUE))
                .andDo(print())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.wasAttended").value(false));
    }


    @Test
    @Order(4)
    void canCancelAppointment () throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/appointment/cancel/1")
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(org.hamcrest.Matchers.equalTo("Appointment has been canceled")));

    }


    @Test
    @Order(5)
    void canDeleteAppointment () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/appointment/1")
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(org.hamcrest.Matchers.equalTo("The appointment has been deleted")));

    }
}