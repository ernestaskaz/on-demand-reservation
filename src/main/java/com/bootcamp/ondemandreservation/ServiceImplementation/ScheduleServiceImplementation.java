package com.bootcamp.ondemandreservation.ServiceImplementation;

import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Model.Schedule;
import com.bootcamp.ondemandreservation.Repository.DoctorRepository;
import com.bootcamp.ondemandreservation.Repository.ScheduleRepository;
import com.bootcamp.ondemandreservation.Service.ScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImplementation implements ScheduleService {

    private ScheduleRepository scheduleRepository;
    private DoctorRepository doctorRepository;

    public ScheduleServiceImplementation(ScheduleRepository scheduleRepository, DoctorRepository doctorRepository) {
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
    }


    @Override
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return null;
    }

    @Override
    public List<Schedule> getScheduleByDoctorId(Long id) {
        return null;
    }

    @Override
    public void updateSchedule(Long id, Schedule schedule) {

    }

    @Override
    public void deleteSchedule(Long id) {

    }

    @Override
    public void setDoctorToSchedule(Long scheduleId, Long doctorId) {
        Doctor currentDoctor =  doctorRepository.findById(doctorId).get();
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        schedule.setDoctor(currentDoctor);
        currentDoctor.addScheduleList(schedule);
        scheduleRepository.save(schedule);
        doctorRepository.save(currentDoctor);


        //save both


    }
}
