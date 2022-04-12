package com.bootcamp.ondemandreservation.serviceimpl;

import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.model.Schedule;
import com.bootcamp.ondemandreservation.repository.AppointmentRepository;
import com.bootcamp.ondemandreservation.repository.DoctorRepository;
import com.bootcamp.ondemandreservation.repository.PatientRepository;
import com.bootcamp.ondemandreservation.service.AppointmentService;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Configuration;
import org.owasp.validator.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.*;
@Configuration
@EnableScheduling

@Service
@Transactional
public class AppointmentServiceImplementation implements AppointmentService {

    public static final Sort SORT_BY_APPOINTMENT_TIME = Sort.by(Sort.Direction.ASC, "appointmentTime","doctorId");
    private Logger log= LoggerFactory.getLogger(AppointmentServiceImplementation.class);
    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private AntiSamy antiSamy;


    public AppointmentServiceImplementation(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, AntiSamy antiSamy) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.antiSamy = antiSamy;
    }



    @Override
    public Map<String,String> validateAppointment(Appointment appointment){
        HashMap<String,String> rv=new HashMap<>();
        try {
            CleanResults scan=antiSamy.scan(appointment.getComment());
            if (scan.getNumberOfErrors()>0){
                StringBuilder sb = new StringBuilder();
                for (String s : scan.getErrorMessages()) {
                    sb.append(s);
                    sb.append(" \n");
                }
                rv.put("comment",sb.toString());
            }
        } catch (Exception x) {
            log.error("AntiSamy error", x);
            rv.put("comment","internal error.");
        }
        return rv;
    }


    /**
     * @param doctorId and patientId passed from Controller to find specific entities.
     * @Method saveAppointment then creates an appointment and maps to patient/doctor.
     * sets isAvaialble and isReserved for appointment so that it is taken and not seen in getAvailableAndNotReserved appointments.
     */

    @Override
    public Appointment saveAppointment(Long doctorId, Long patientId) {
        Doctor doctor = doctorRepository.findById(doctorId).get();
        Patient patient = patientRepository.findById(patientId).get();

        Appointment appointment = new Appointment(doctor, patient);

        appointment.setAvailable(false);
        appointment.setReserved(true);

        doctor.addAppointmentList(appointment);
        patient.addAppointmentList(appointment);
        appointmentRepository.save(appointment);

        return appointment;


    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll(SORT_BY_APPOINTMENT_TIME);
    }

    @Override
    public List<Appointment> getAllAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId, SORT_BY_APPOINTMENT_TIME);
    }
    @Override
    public List<Appointment> getPastAppointmentsByDoctorId(Long doctorId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.of(2000, Month.JULY, 1, 00, 00, 00);
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start,now, SORT_BY_APPOINTMENT_TIME);
    }

    @Override
    public List<Appointment> findAvailableAndNotReserved() {
        return appointmentRepository.findByIsAvailableTrueAndIsReservedFalseAndAppointmentTimeIsAfter(LocalDateTime.now(),SORT_BY_APPOINTMENT_TIME);
    }

    /**
     * Method generates all appointments for today for an Admin role view.
     */

    @Override
    public List<Appointment> getTodaysAppointments() {
        //return appointmentRepository.findAll()  threading/concurrency problem - multiple users.  takes time, loads service and db.
//        DB automatically has built in concurrency safeguards.
//        List<Appointment> AllAppointments = appointmentRepository.findAll(Sort.by(Sort.Direction.ASC,"appointmentTime","id"));
//        List<Appointment> todaysAppointments = new ArrayList<>();
//
//        for (Appointment appointment: AllAppointments) {
//
//            if(appointment.getAppointmentTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth() && appointment.getAppointmentTime().isAfter(LocalDateTime.now()) ) {
//                todaysAppointments.add(appointment);
//            }
//        }

        return appointmentRepository.findByAppointmentTimeBetween(LocalDateTime.now(),LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS), SORT_BY_APPOINTMENT_TIME);
    }
    @Override
    public List<Appointment> getUpcomingAppointmentsForTodayByDoctorId(Long id) {
        LocalDateTime now=LocalDateTime.now();
        return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(id, now,now.plusDays(1).truncatedTo(ChronoUnit.DAYS), SORT_BY_APPOINTMENT_TIME);
    }


    @Override
    public Appointment getAppointmentById(Long appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        return appointment.get();
    }

    /**
     * This method cancels appointment by removing patient(another patient can be set up);
     * sets reserved to false so that searches would show this appointment;
     * sets appointment to true so that searches would show this appointment;
     */

    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment currentAppointment = getAppointmentById(appointmentId);
        currentAppointment.removePatient();
        currentAppointment.setReserved(false);
        currentAppointment.setAvailable(true);
        currentAppointment.setConfirmed(false);
        saveAppointment(currentAppointment);

    }

    @Override
    public boolean cancelAppointment(Long patientId, Long appointmentId) {
        Appointment currentAppointment = getAppointmentById(appointmentId);
        if(currentAppointment.getPatient().getId()==patientId){
            cancelAppointment(appointmentId);
            return true;
        }
        return false;
    }

    @Override
    public void deleteAppointment(Long appointmentId) {
        appointmentRepository.deleteById(appointmentId);

    }

    @Transactional
    @Override
    public boolean flipAppointmentAvailable(Long appointmentId) {
        Appointment currentAppointment = getAppointmentById(appointmentId);
        currentAppointment.setAvailable(!currentAppointment.isAvailable());
        currentAppointment=saveAppointment(currentAppointment);
        return currentAppointment.isAvailable();
    }
    @Transactional
    @Override
    public void setAppointmentWasAttended(Long appointmentId) {
        Appointment currentAppointment = getAppointmentById(appointmentId);
        currentAppointment.setWasAttended(true);
        saveAppointment(currentAppointment);
    }
    @Transactional
    @Override
    public void addComment(Long appointmentId, String comment) {
        Appointment currentAppointment = getAppointmentById(appointmentId);
        currentAppointment.setComment(comment);
        saveAppointment(currentAppointment);
    }

    @Transactional
    @Override
    public void reserveAppointment(Long patientId, Long appointmentId) {
        Appointment currentAppointment = getAppointmentById(appointmentId);
        Patient currentPatient = patientRepository.findById(patientId).get();
        if(!currentAppointment.isReserved()){
            currentAppointment.setReserved(true);
            currentAppointment.setAvailable(false);
            currentAppointment.setPatient(currentPatient);
            saveAppointment(currentAppointment);
        }else throw new IllegalArgumentException("Appointment already reserved.");

    }
    /**
     *  This method iterates through Doctor List and automatically calls generateAppointmentsBySchedule() method for each doctor.
     *  Automatic generation happens on (cron = "{value for seconds} {value for mintues} {value for hours} {value for days} * ?")
     */



    @Scheduled(cron = "0 30 14 12 * ?")
    public void automaticAppointmentGeneration() {
        List<Doctor> listOfDoctors = doctorRepository.findAll();

        for (Doctor doctor: listOfDoctors) {
            generateAppointmentsBySchedule(doctor.getId(), 14);
        }
    }

    /**
     * @param daysCount passed from Controller specifies for how many days should appointments be generated.
     * @param doctorId finds doctor for which appointments are generated and sets this doctor to newly created appointments.
     *  This method iterates through currentDoctor schedules and adds appointments that are on specific day of the week
     *  and between schedule.startHour and endHour for that specific day.
     *  appointments are hourly.
     */


    @Override
    public void generateAppointmentsBySchedule(Long doctorId, int daysCount) {

        Doctor currentDoctor = doctorRepository.findById(doctorId).get();
        DateTimeFormatter toStringDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter setHoursToZero = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00");

        LocalDateTime originalLocalDateTime = LocalDateTime.now();
        // set generation at 00:00.
        String zeroHoursAndMinutes = originalLocalDateTime.format(setHoursToZero);
        // creates LocalDateTime start point for  appointment generation.
        LocalDateTime startPoint = LocalDateTime.parse(zeroHoursAndMinutes, toStringDateFormatter);
        LocalDateTime originalPoint = startPoint;
        // creates LocalDateTime end point for appointment generation.
        LocalDateTime endPoint = startPoint.plusDays(daysCount);

        List<Appointment> doctorAppointmentList = getAllAppointmentsByDoctorId(currentDoctor.getId());
        List<Schedule> doctorScheduleList = currentDoctor.getSchedulesList();

        if (doctorAppointmentList.size() != 0) {
            // get list and sort it.
            //get last date
            LocalDateTime lastAppointmentTime = doctorAppointmentList.get(doctorAppointmentList.size() -1).getAppointmentTime();
            LocalDateTime firstAppointmentTime = doctorAppointmentList.get(0).getAppointmentTime();
            //Last appointment isi now start point.
            startPoint = lastAppointmentTime;
            originalPoint = startPoint;
            // generates for the next 10 days.
            endPoint = startPoint.plusDays(14);
            //set start date to new date that is missing from days count.

        }

        for (Schedule schedule : doctorScheduleList) {

            startPoint = originalPoint;
            while(startPoint.isBefore(endPoint)) {


                if (schedule.getDayOfWeek().equals(startPoint.getDayOfWeek()) && startPoint.getHour() >= schedule.getStartHour() && startPoint.getHour() < schedule.getEndHour() && startPoint.getHour() != schedule.getLunchTime()) {
                    Appointment appointment = new Appointment(startPoint, currentDoctor);
                        currentDoctor.addAppointmentList(appointment);
                        saveAppointment(appointment);
                }
                startPoint = startPoint.plusHours(1);

        }

        }

    }

    @Override
    public Appointment updateAppointment(Long appointmentId, Appointment appointment) {
        Appointment originalAppointment = getAppointmentById(appointmentId);
        appointment.setId(appointmentId);
        appointment.setPatient(originalAppointment.getPatient());
        appointment.setDoctor(originalAppointment.getDoctor());
       return saveAppointment(appointment);

    }



}
