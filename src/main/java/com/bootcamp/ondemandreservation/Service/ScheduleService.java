package com.bootcamp.ondemandreservation.Service;

import com.bootcamp.ondemandreservation.Model.Schedule;

import java.util.List;

public interface ScheduleService {

    Schedule saveSchedule(Schedule schedule);
    List<Schedule> getAllSchedules();
    List<Schedule> getScheduleByDoctorId(Long id);
    void updateSchedule(Long id, Schedule schedule);
    void deleteSchedule(Long id);
    void setDoctorToSchedule(Long scheduleId, Long doctorId);
}
