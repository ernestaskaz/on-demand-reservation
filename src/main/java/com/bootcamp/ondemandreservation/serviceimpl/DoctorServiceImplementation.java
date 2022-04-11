package com.bootcamp.ondemandreservation.serviceimpl;

import com.bootcamp.ondemandreservation.model.*;
import com.bootcamp.ondemandreservation.repository.AppointmentRepository;
import com.bootcamp.ondemandreservation.repository.DoctorRepository;
import com.bootcamp.ondemandreservation.security.ODRInputSanitiser;
import com.bootcamp.ondemandreservation.service.AppointmentService;
import com.bootcamp.ondemandreservation.service.DoctorService;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.service.ODRUserService;
import com.bootcamp.ondemandreservation.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;


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
    @Autowired
    private AppointmentRepository appointmentRepository;

    public DoctorServiceImplementation() {
    }

    public DoctorServiceImplementation(DoctorRepository doctorRepository, ODRPasswordEncoder odrPasswordEncoder,
                                       ODRUserService odrUserService, ScheduleService scheduleService,
                                       AppointmentService appointmentService,
                                       AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.odrPasswordEncoder = odrPasswordEncoder;
        this.odrUserService = odrUserService;
        this.scheduleService = scheduleService;
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    @Override
    public void changePassword(Long id, String plaintextPassword) {
        Doctor theDoctor=findDoctorById(id);
        theDoctor.setPassword(odrPasswordEncoder.defaultPasswordEncoder().encode(plaintextPassword));
        saveDoctor(theDoctor);
    }

    /**
     * Save doctor method
     * @param doctor doctor to save
     * @param createSchedule true if need to create default schedule for new doctor
     * @return Doctor as saved in the DB, nul;l if error
     */
    @Transactional
    @Override
    public Doctor saveDoctor(Doctor doctor, boolean createSchedule) {
        Doctor newDoctor=doctorRepository.save(doctor);
        if(createSchedule)
            generateDefaultSchedules(newDoctor);
        return newDoctor;
    }

    /**
     * Save doctor method
     * note that b/c we're using an interface
     * \@Transactional of methods we call is ignored, so we need a separate one.
     * @param doctor doctor to save
     * @return Doctor as saved in the DB, nul;l if error
     */
    @Transactional
    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return saveDoctor(doctor,false);
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
    @Transactional
    @Override
    public void deleteDoctor(Long id) {
        Doctor currentDoctor = findDoctorById(id);
        currentDoctor.removeDoctorFromAppointmentList();
        currentDoctor.removeDoctorFromSchedule();
        doctorRepository.deleteById(id);
    }

    @Override
    public List<Appointment> getAllAppointments(Long id) {
        return appointmentRepository.findByDoctorId(id,Sort.by(Sort.Direction.ASC,"appointmentTime"));
    }

    @Override
    public List<Schedule> getDoctorSchedules(Long id) {
        Doctor doctor = findDoctorById(id);
        return doctor.getSchedulesList();
    }

    @Override
    public List<Appointment> getDoctorPastAppointments(Long id) {
        List<Appointment> AllAppointments = getAllAppointments(id);
        List<Appointment> pastAppointments = new ArrayList<>();

        for (Appointment appointment: AllAppointments) {

            if(appointment.getAppointmentTime().isBefore(LocalDateTime.now())) {
                pastAppointments.add(appointment);
            }

        }

        return pastAppointments;

    }
    @Transactional
    @Override
    public Doctor updateDoctor(Long id, Doctor doctor) {
        doctor.setId(id);
        return saveDoctor(doctor, false);
    }

    @Override
    public List<Appointment> getUpcomingAppointmentsForToday(Long id) {
        LocalDateTime now=LocalDateTime.now();
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(id, now,now.plusDays(1).truncatedTo(ChronoUnit.DAYS),Sort.by(Sort.Direction.ASC,"appointmentTime"));
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
        Map<String, String> rv = new HashMap<>(odrUserService.validate(doctor,matchPassword,forUpdate));
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
    @Transactional
    @Override
    public Doctor saveDoctorAndPassword(Doctor doctor, boolean createSchedule) {
        doctor.setPassword(odrPasswordEncoder.defaultPasswordEncoder()
                .encode(doctor.getPassword()));

        Doctor savedDoctor = saveDoctor(doctor);
        if(createSchedule)
            generateDefaultSchedules(savedDoctor);

        return savedDoctor;
    }

    /**
     * should only be called from @Transactional methods as non-public methods ignore @Transactional by themselves
     */
    protected void generateDefaultSchedules(Doctor doctor) {
        Schedule scheduleMonday = new Schedule(DayOfWeek.MONDAY, 8, 19, 13);
        Schedule scheduleTuesday = new Schedule(DayOfWeek.TUESDAY, 8, 19, 13);
        Schedule scheduleWednesday = new Schedule(DayOfWeek.WEDNESDAY, 8, 19, 13);
        Schedule scheduleThursday = new Schedule(DayOfWeek.THURSDAY, 8, 19, 13);
        Schedule scheduleFriday = new Schedule(DayOfWeek.FRIDAY, 8, 19, 13);

        scheduleMonday.setDoctor(doctor);
        scheduleTuesday.setDoctor(doctor);
        scheduleWednesday.setDoctor(doctor);
        scheduleThursday.setDoctor(doctor);
        scheduleFriday.setDoctor(doctor);

        scheduleService.saveSchedule(scheduleMonday);
        scheduleService.saveSchedule(scheduleTuesday);
        scheduleService.saveSchedule(scheduleWednesday);
        scheduleService.saveSchedule(scheduleThursday);
        scheduleService.saveSchedule(scheduleFriday);
    }
}
