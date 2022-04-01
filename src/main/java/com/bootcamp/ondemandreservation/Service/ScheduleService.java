package com.bootcamp.ondemandreservation.Service;

import com.bootcamp.ondemandreservation.Model.Schedule;

import java.util.List;

public interface ScheduleService {

    Schedule saveSchedule(Schedule schedule);
    List<Schedule> getScheduleByDoctorId(Long id);
}
