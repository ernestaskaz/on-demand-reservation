package com.bootcamp.ondemandreservation.Controller;

import com.bootcamp.ondemandreservation.Helpers;
import com.bootcamp.ondemandreservation.Model.Patient;
import com.bootcamp.ondemandreservation.Model.Schedule;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.DayOfWeek;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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


}
