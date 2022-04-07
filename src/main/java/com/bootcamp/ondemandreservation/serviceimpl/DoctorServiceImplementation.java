package com.bootcamp.ondemandreservation.serviceimpl;

import com.bootcamp.ondemandreservation.model.*;
import com.bootcamp.ondemandreservation.repository.DoctorRepository;
import com.bootcamp.ondemandreservation.security.ODRInputSanitiser;
import com.bootcamp.ondemandreservation.service.AppointmentService;
import com.bootcamp.ondemandreservation.service.DoctorService;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.service.ODRUserService;
import com.bootcamp.ondemandreservation.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class DoctorServiceImplementation implements DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private ODRPasswordEncoder odrPasswordEncoder;
    @Autowired
    private ODRUserService odrUserService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private AppointmentService appointmentService;


    public DoctorServiceImplementation() {
    }

    public DoctorServiceImplementation(DoctorRepository doctorRepository, ODRPasswordEncoder odrPasswordEncoder, ODRUserService odrUserService, ScheduleService scheduleService, AppointmentService appointmentService) {
        this.doctorRepository = doctorRepository;
        this.odrPasswordEncoder = odrPasswordEncoder;
        this.odrUserService = odrUserService;
        this.scheduleService = scheduleService;
        this.appointmentService = appointmentService;
    }

    @Override
    public void changePassword(Long id, String plaintextPassword) {
        Doctor theDoctor=findDoctorById(id);
        theDoctor.setPassword(odrPasswordEncoder.defaultPasswordEncoder().encode(plaintextPassword));
        saveDoctor(theDoctor);
    }

    /**
     * Save doctor method
     * @param doctor doctor to save
     * @return Doctor as saved in the DB, nul;l if error
     */
    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor findDoctorById(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return doctor.get();
    }

    @Override
    public Doctor getLoggedInDoctor() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id=null;
        if (principal instanceof ODRUser) {
            id = ((ODRUser)principal).getId();
        } else {
            throw new ODRUserNotFoundException();
        }
        Doctor doctor = findDoctorById(id);
        if(doctor==null){
            throw new ODRUserNotFoundException();
        }
        return doctor;
    }


    /**
     * @param id passed from Controller to find specific doctor.
     *  To delete a Doctor object, doctor can not have any relations to other tables.
     *  This method removes relations to any Appointment list that a doctor might have relations to.
     *  Deletes doctor.
     */

    @Override
    public void deleteDoctor(Long id) {
        Doctor currentDoctor = findDoctorById(id);
        currentDoctor.removeDoctorFromAppointmentList();
        currentDoctor.removeDoctorFromSchedule();
        doctorRepository.deleteById(id);
    }

    @Override
    public List<Appointment> getAllAppointments(Long id) {
        Doctor doctor = findDoctorById(id);
        return doctor.getAppointmentList();
    }

    @Override
    public List<Schedule> getDoctorSchedules(Long id) {
        Doctor doctor = findDoctorById(id);
        return doctor.getSchedulesList();
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor doctor) {
        doctor.setId(id);
        return saveDoctor(doctor);
    }

    @Override
    public List<Appointment> getTodaysAppointments(Long id) {
        List<Appointment> AllAppointments = getAllAppointments(id);
        List<Appointment> todaysAppointments = new ArrayList<>();

        for (Appointment appointment: AllAppointments) {

            if(appointment.getAppointmentTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) {
                todaysAppointments.add(appointment);
            }

        }

        return todaysAppointments;
    }

    /**
     * Validates the doctor
     * Needs some specialty validation later
     * @param doctor        Doctor to be validated
     * @param matchPassword if we should check for password and confirmPassword to match.
     * @return Map with fields (email, not getEmail) as keys and error messages as values
     */
    @Override
    public Map<String, String> validateDoctor(Doctor doctor, boolean matchPassword,boolean forUpdate) {
        Map rv=odrUserService.validate(doctor,matchPassword,forUpdate);
        if(doctor.getSpecialty()!=null&&!ODRInputSanitiser.seemsToBeSafe(doctor.getSpecialty())){
            rv.put("specialty", "unsuitable");
        }
        return rv;
    }
    /**
     * The same as saveDoctor, but the password is treated as plain text
     * and is hashed&salted before saving
     * @param doctor Doctor model to save
     * @return doctor with hashed password
     */

    @Override
    public Doctor saveDoctorAndPassword(Doctor doctor) {
        doctor.setPassword(odrPasswordEncoder.defaultPasswordEncoder()
                .encode(doctor.getPassword()));

        Doctor savedDoctor = saveDoctor(doctor);

        Schedule scheduleMonday = new Schedule(DayOfWeek.MONDAY, 12, 19, 15);
        Schedule scheduleFriday = new Schedule(DayOfWeek.FRIDAY, 12, 19, 15);

        scheduleFriday.setDoctor(savedDoctor);
        scheduleMonday.setDoctor(savedDoctor);

        scheduleService.saveSchedule(scheduleFriday);
        scheduleService.saveSchedule(scheduleMonday);

        return savedDoctor;
    }
}
