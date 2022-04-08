package com.bootcamp.ondemandreservation.serviceimpl;

import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.model.Schedule;
import com.bootcamp.ondemandreservation.repository.AppointmentRepository;
import com.bootcamp.ondemandreservation.repository.DoctorRepository;
import com.bootcamp.ondemandreservation.repository.PatientRepository;
import com.bootcamp.ondemandreservation.service.AppointmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImplementation implements AppointmentService {

    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;

    public AppointmentServiceImplementation(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
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
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> findAvailableAndNotReserved() {
        return appointmentRepository.findByIsAvailableTrueAndIsReservedFalse();
    }

    @Override
    public List<Appointment> getTodaysAppointments() {
        List<Appointment> AllAppointments = getAllAppointments();
        List<Appointment> todaysAppointments = new ArrayList<>();

        for (Appointment appointment: AllAppointments) {

            if(appointment.getAppointmentTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) {
                todaysAppointments.add(appointment);
            }

        }

        return todaysAppointments;
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

    @Override
    public void setAppointmentUnavailable(Long appointmentId) {
        Appointment currentAppointment = getAppointmentById(appointmentId);
        currentAppointment.setAvailable(false);
        saveAppointment(currentAppointment);
    }

    @Override
    public void setAppointmentWasAttended(Long appointmentId) {
        Appointment currentAppointment = getAppointmentById(appointmentId);
        currentAppointment.setWasAttended(true);
        saveAppointment(currentAppointment);
    }

    @Override
    public void addComment(Long appointmentId, String comment) {
        Appointment currentAppointment = getAppointmentById(appointmentId);
        currentAppointment.setComment(comment);
        saveAppointment(currentAppointment);
    }

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
        //start generating appointments from the next day.
        LocalDateTime addOneDay = LocalDateTime.now().plusDays(1);
        // set generation at 00:00.
        String zeroHoursAndMinutes = addOneDay.format(setHoursToZero);
        // creates LocalDateTime start point for  appointment generation.
        LocalDateTime startPoint = LocalDateTime.parse(zeroHoursAndMinutes, toStringDateFormatter);
        LocalDateTime originalPoint = startPoint;
        // creates LocalDateTime end point for appointment generation.
        LocalDateTime endPoint = startPoint.plusDays(daysCount);


        for (Schedule schedule : currentDoctor.getSchedulesList()) {

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
