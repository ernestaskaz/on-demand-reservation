package com.bootcamp.ondemandreservation.Model;


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

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointmentList = new ArrayList<Appointment>();

    @OneToMany(mappedBy = "doctor")
    private List<Schedule> schedules = new ArrayList<Schedule>();

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public Doctor(){

    }

    public void addAppointmentList(Appointment appointment) {
        this.appointmentList.add(appointment);
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
