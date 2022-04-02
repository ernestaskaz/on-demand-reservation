package com.bootcamp.ondemandreservation.Model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String specialty;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointmentList = new ArrayList<Appointment>();

    @OneToMany(mappedBy = "doctor")
    @JsonIgnoreProperties({"doctor"})
    private List<Schedule> schedulesList = new ArrayList<Schedule>();

    public Doctor(){

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
