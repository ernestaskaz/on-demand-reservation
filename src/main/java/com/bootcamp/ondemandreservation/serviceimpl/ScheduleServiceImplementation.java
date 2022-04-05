package com.bootcamp.ondemandreservation.serviceimpl;

import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Schedule;
import com.bootcamp.ondemandreservation.repository.DoctorRepository;
import com.bootcamp.ondemandreservation.repository.ScheduleRepository;
import com.bootcamp.ondemandreservation.service.ScheduleService;
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
    public Schedule findScheduleById(Long id) {
        return scheduleRepository.findById(id).get();
    }

    @Override
    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }


    @Override
    public Schedule updateSchedule(Long id, Schedule schedule) {
        Schedule dbSchedule = findScheduleById(id);
        Doctor currentDoctor = dbSchedule.getDoctor();
        schedule.setId(id);
        schedule.setDoctor(currentDoctor);
        return saveSchedule(schedule);


    }

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

    @Override
    public void setDoctorToSchedule(Long scheduleId, Long doctorId) {
        Doctor currentDoctor =  doctorRepository.findById(doctorId).get();
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        schedule.setDoctor(currentDoctor);
        currentDoctor.addScheduleList(schedule);
        scheduleRepository.save(schedule);
        doctorRepository.save(currentDoctor);

    }
}
