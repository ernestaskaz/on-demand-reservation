package com.bootcamp.ondemandreservation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Patient extends ODRUser{
    private String city;
    private int age;
    private String idCard;
    private String phoneNumber;
    private boolean priorityQueue;
    @JsonIgnoreProperties("doctor")
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointmentList = new ArrayList<Appointment>();

    public Patient () {

    }

    public Patient(Long id, String firstName, String lastName) {
        super(id,firstName,lastName);
    }



    public void removePatientFromAppointmentList() {
        for (Appointment appointment: appointmentList) {
            appointment.removePatient();
        }
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public boolean isPriorityQueue() {
        return priorityQueue;
    }

    public void setPriorityQueue(boolean priorityQueue) {
        this.priorityQueue = priorityQueue;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public void addAppointmentList(Appointment appointment) {
        this.appointmentList.add(appointment);
    }



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


}