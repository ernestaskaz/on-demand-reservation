package com.bootcamp.ondemandreservation.restcontroller;

import com.bootcamp.ondemandreservation.Helpers;

import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Doctor;

import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.model.Schedule;
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
public class DoctorControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username="admin@default.com")
    @Order(1)
    void canSaveDoctor() throws Exception {

        Doctor doctor = new Doctor("this is name", "this is lastName", "Specialty");
        Schedule schedule = new Schedule();
        doctor.addScheduleList(schedule);
        Appointment appointment = new Appointment();
        doctor.addAppointmentList(appointment);
        doctor.addAppointmentList(appointment);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/doctor")
                        .content(Helpers.asJsonString(doctor))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("this is name"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/doctor")
                        .content(Helpers.asJsonString(doctor))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("this is name"));
    }

    @Test
    @WithMockUser(username="admin@default.com")
    @Order(3)
    void canFindDoctorById() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/doctor/2")
                        .accept(MediaType.ALL_VALUE))
                .andDo(print())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.specialty").value("Specialty"));
    }

    @Test
    @WithMockUser(username="admin@default.com")
    @Order(2)
    void canGetAllDoctors () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/doctor")
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].specialty").value("Specialty"));

    }

    @Test
    @WithMockUser(username="admin@default.com")
    @Order(4)
    void canUpdateDoctor () throws Exception {

        Doctor newDoctor = new Doctor("this is name", "this is lastName", "SpecialtyChanged");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/doctor/2")
                        .content(Helpers.asJsonString(newDoctor))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.specialty").value("SpecialtyChanged"));

    }



//    @Test
//    @Order(5)
//    void canGetAppointments () throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/doctor/appointments/2")
//                        .accept(MediaType.ALL_VALUE))
//                .andDo(print())
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(jsonPath("$.appointmentList").isNotEmpty());
//
//    }


    @Test
    @WithMockUser(username="admin@default.com")
    @Order(6)
    void canDeleteDoctorById () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/doctor/2")
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(org.hamcrest.Matchers.equalTo("Doctor successfully deleted")));

    }


//    @Test
//    @Order(7)
//    void canGetSchedules () throws Exception {
//
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/doctor/2")
//                        .accept(MediaType.ALL_VALUE))
//                .andDo(print())
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(jsonPath("$.specialty").value("Specialty"));


//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/doctor/schedule/2")
//                        .accept(MediaType.ALL_VALUE))
//                .andDo(print())
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(jsonPath("$.specialty").value("Specialty"));


//                .andExpect(jsonPath("$.schedulesList").isNotEmpty());
//        Doctor doctor = new Doctor(1L, "this is name", "this is lastName", "Specialty");
//        Schedule schedule = new Schedule();
//        doctor.addScheduleList(schedule);
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/doctor/2")
//                        .content(Helpers.asJsonString(doctor))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(jsonPath("$.specialty").value("Specialty"));
//
//    }


}
