package com.bootcamp.ondemandreservation.ServiceImplementation;

import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Model.Patient;
import com.bootcamp.ondemandreservation.Model.Schedule;
import com.bootcamp.ondemandreservation.Repository.AppointmentRepository;
import com.bootcamp.ondemandreservation.Repository.DoctorRepository;
import com.bootcamp.ondemandreservation.Repository.PatientRepository;
import com.bootcamp.ondemandreservation.Service.AppointmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public void deleteAppointment(Long appointmentId) {
        appointmentRepository.deleteById(appointmentId);

    }

    /**
     * @param endDate passed from Controller specifies for how many days should appointments be generated.
     * @param doctorId finds doctor for which appointments are generated and sets this doctor to newly created appointments.
     *  This method iterates through currentDoctor schedules and adds appointments that are on specific day of the week
     *  and between schedule.startHour and endHour for that specific day.
     *  appointments are hourly.
     */

    @Override
    public void generateAppointmentsBySchedule(Long doctorId, int endDate) {

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
        LocalDateTime endPoint = startPoint.plusDays(endDate);


        for (Schedule schedule : currentDoctor.getSchedulesList()) {

            startPoint = originalPoint;
            while(startPoint.isBefore(endPoint)) {


                if (schedule.getDayOfWeek().equals(startPoint.getDayOfWeek()) && startPoint.getHour() >= schedule.getStartHour() && startPoint.getHour() <= schedule.getEndHour() && startPoint.getHour() != schedule.getLunchTime()) {
                    Appointment appointment = new Appointment(startPoint, currentDoctor);
                    currentDoctor.addAppointmentList(appointment);
                    saveAppointment(appointment);
                }
                startPoint = startPoint.plusHours(1);

        }

        }

    }
}
