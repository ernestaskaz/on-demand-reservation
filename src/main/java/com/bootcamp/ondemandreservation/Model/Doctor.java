package com.bootcamp.ondemandreservation.Model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Doctor extends ODRUser{

    private String specialty;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointmentList = new ArrayList<Appointment>();

    @OneToMany(mappedBy = "doctor")
    @JsonIgnoreProperties({"doctor"})
    private List<Schedule> schedulesList = new ArrayList<Schedule>();

    public Doctor(){

    }

    public Doctor(Long id, String firstName, String lastName, String specialty) {
        super(id,firstName,lastName);
        this.specialty = specialty;
    }

    public Doctor(String firstName, String lastName, String specialty) {
        super(firstName,lastName);
        this.specialty = specialty;
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
