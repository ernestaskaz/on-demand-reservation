package com.bootcamp.ondemandreservation.restcontroller;

import com.bootcamp.ondemandreservation.Helpers;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Schedule;
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

import java.time.DayOfWeek;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ScheduleControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @Test
    @Order(1)
    void canSaveSchedule() throws Exception {
        Schedule schedule = new Schedule(DayOfWeek.MONDAY, 12, 16);

        mockMvc.perform(MockMvcRequestBuilders.post("/schedule")
                        .content(Helpers.asJsonString(schedule))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dayOfWeek").value("MONDAY"));

    }

    @Test
    @Order(2)
    void canGetAllSchedules() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/schedule")
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].dayOfWeek").value("MONDAY"));
    }

    @Test
    @Order(3)
    void canUpdateSchedule () throws Exception {

        Schedule schedule = new Schedule(DayOfWeek.TUESDAY, 9, 15);
        mockMvc.perform(MockMvcRequestBuilders.put("/schedule/1")
                        .content(Helpers.asJsonString(schedule))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.dayOfWeek").value("TUESDAY"));

    }

    @Test
    @Order(4)
    void canSetDoctorToSchedule() throws Exception {

        //since we need doctor to perform  this test, we create and save doctor using its controllerTest;
        Doctor doctor = new Doctor(1L,"this is name", "this is lastName", "Specialty");

        mockMvc.perform(MockMvcRequestBuilders.post("/doctor")
                        .content(Helpers.asJsonString(doctor))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.specialty").value("Specialty"));


        mockMvc.perform(MockMvcRequestBuilders.put("/schedule/1/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().string(org.hamcrest.Matchers.equalTo("Schedule has been added to selected doctor")));

    }



    @Test
    @Order(5)
    void canDeletePatient () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/schedule/1")
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(org.hamcrest.Matchers.equalTo("Schedule successfully deleted")));

    }


}
