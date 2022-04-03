package com.bootcamp.ondemandreservation.Service;

import com.bootcamp.ondemandreservation.Model.Schedule;

import java.util.List;

public interface ScheduleService {

    Schedule saveSchedule(Schedule schedule);
    List<Schedule> getAllSchedules();
    Schedule updateSchedule(Long id, Schedule schedule);
    Schedule findScheduleById(Long id);
    void deleteScheduleById(Long id);
    void setDoctorToSchedule(Long scheduleId, Long doctorId);
}
