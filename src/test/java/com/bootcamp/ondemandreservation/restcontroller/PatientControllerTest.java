package com.bootcamp.ondemandreservation.restcontroller;

import com.bootcamp.ondemandreservation.Helpers;
import com.bootcamp.ondemandreservation.model.Patient;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PatientControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username="admin@default.com",authorities="ROLE_ADMIN")
    @Order(1)
    void canSavePatient() throws Exception {

        Patient patient = new Patient(1L, "firstName", "lastName");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/patient")
                        .content(Helpers.asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("firstName"));


    }

    @Test
    @WithMockUser(username="admin@default.com",authorities="ROLE_ADMIN")
    @Order(3)
    void canFindPatientById() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/patient/2")
                        .accept(MediaType.ALL_VALUE))
                .andDo(print())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.firstName").value("firstName"));
    }

    @Test
    @WithMockUser(username="admin@default.com",roles={"ADMIN_ROLE"})
    @Order(2)
    void canGetAllPatients () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/patient")
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].firstName").value("firstName"));

    }

    @Test
    @WithMockUser(username="admin@default.com",authorities="ROLE_ADMIN")
    @Order(4)
    void canUpdatePatient () throws Exception {

        Patient patient = new Patient(1L, "firstName", "updatedLastName");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/patient/2")
                        .content(Helpers.asJsonString(patient))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.lastName").value("updatedLastName"));

    }


    @Test
    @WithMockUser(username="admin@default.com",authorities="ROLE_ADMIN")
    @Order(5)
    void canDeletePatient () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/patient/2")
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(org.hamcrest.Matchers.equalTo("Patient successfully deleted")));

    }
}
