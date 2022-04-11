package com.bootcamp.ondemandreservation.service;

import com.bootcamp.ondemandreservation.model.Schedule;

import java.util.List;
import java.util.Map;

public interface ScheduleService {

    Schedule saveSchedule(Schedule schedule);
    List<Schedule> getAllSchedules();
    Schedule updateSchedule(Long id, Schedule schedule);
    Schedule findScheduleById(Long id);
    void deleteScheduleById(Long id);
    void setDoctorToSchedule(Long scheduleId, Long doctorId);
    Map<String, String> validateSchedule(Schedule schedule);
}
