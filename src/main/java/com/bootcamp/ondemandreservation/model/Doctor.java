package com.bootcamp.ondemandreservation.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Doctor extends ODRUser{
    public static final String DOCTOR_ROLE = "hasRole('ROLE_DOCTOR')";

    private String specialty;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointmentList = new ArrayList<Appointment>();

    @OneToMany(mappedBy = "doctor")
    @JsonIgnoreProperties({"doctor"})
    private List<Schedule> schedulesList = new ArrayList<Schedule>();
    {
        setAccountType("DOCTOR");
    }

    public Doctor(){

    }

    public Doctor(ODRUser user, String specialty, List<Appointment> appointmentList, List<Schedule> schedulesList) {
        super(user);
        this.specialty = specialty;
        this.appointmentList = appointmentList;
        this.schedulesList = schedulesList;
    }

    public Doctor(Long id, String firstName, String lastName, String specialty) {
        super(id,firstName,lastName);
        this.specialty = specialty;
    }

    public Doctor(String firstName, String lastName, String specialty) {
        super(firstName,lastName);
        this.specialty = specialty;
    }
    public Doctor(String firstName, String lastName, String specialty, List<Schedule> schedule, List<Appointment> appointmentList) {
        super(firstName,lastName);
        this.specialty = specialty;
        this.schedulesList = schedule;
        this.appointmentList = appointmentList;
    }




    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }


    public void removeDoctorFromAppointmentList() {
        for (Appointment appointment: appointmentList) {
            appointment.removeDoctor();
        }
    }

    public void addAppointmentList(Appointment appointment) {
        this.appointmentList.add(appointment);
    }

    public List<Schedule> getSchedulesList() {
        return schedulesList;
    }

    public void addScheduleList(Schedule schedule) {
        this.schedulesList.add(schedule);
    }
    public void removeDoctorFromSchedule() {
        for (Schedule schedule : schedulesList) {
            schedule.removeDoctor();
        }
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }



}
