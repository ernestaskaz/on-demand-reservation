package com.bootcamp.ondemandreservation.serviceimpl;

import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.ODRUser;
import com.bootcamp.ondemandreservation.model.Schedule;
import com.bootcamp.ondemandreservation.repository.DoctorRepository;
import com.bootcamp.ondemandreservation.repository.ScheduleRepository;
import com.bootcamp.ondemandreservation.security.ODRInputSanitiser;
import com.bootcamp.ondemandreservation.service.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.*;

@Service
public class ScheduleServiceImplementation implements ScheduleService {

    private ScheduleRepository scheduleRepository;
    private DoctorRepository doctorRepository;

    public ScheduleServiceImplementation(ScheduleRepository scheduleRepository, DoctorRepository doctorRepository) {
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Transactional
    @Override
    public Schedule updateSchedule(Long id, Schedule schedule) {
        Schedule dbSchedule = findScheduleById(id);
        Doctor currentDoctor = dbSchedule.getDoctor();
        schedule.setId(id);
        schedule.setDoctor(currentDoctor);
        return saveSchedule(schedule);


    }
    @Transactional
    @Override
    public void deleteScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id).get();
        schedule.removeDoctor();
        scheduleRepository.deleteById(id);

    }

    /**
     * @param doctorId takes in already existing doctor. has to be created before hand.
     * @param scheduleId takes in already exististing schedule (day). has to be created before hand.
     * maps doctor to schedule.
     */
    @Transactional
    @Override
    public void setDoctorToSchedule(Long scheduleId, Long doctorId) {
        Doctor currentDoctor =  doctorRepository.findById(doctorId).get();
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        schedule.setDoctor(currentDoctor);
        currentDoctor.addScheduleList(schedule);
        scheduleRepository.save(schedule);
        doctorRepository.save(currentDoctor);

    }

    @Override
    public Map<String, String> validateSchedule(Schedule schedule) {
        Map<String,String> rv=new HashMap<>();
        if(schedule.getStartHour() < 1 || schedule.getStartHour() > 23) {
            rv.put("startHour", "Please provide a start hour(number) between 1 and 23");
        }

        if(schedule.getEndHour() < 1 || schedule.getEndHour() > 23) {
            rv.put("endHour", "Please provide an end hour(number) between 1 and 23");
        }

        if(schedule.getLunchTime() <= schedule.getStartHour() || schedule.getLunchTime() >= schedule.getEndHour()) {
            rv.put("lunchTime", "Please provide a lunch hour that is between Start Hour and End Hour");
        }
        return rv;
    }
}
