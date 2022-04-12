package com.bootcamp.ondemandreservation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

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

    public static final String PATIENT_ROLE = "hasRole('ROLE_PATIENT')";
    public static final String PATIENT_ROLE_SAME_ID = "hasRole('ROLE_PATIENT') and #patient.id == authentication.principal.id";
    {
        setAccountType("PATIENT");
    }

    public Patient () {
    }

    public Patient(Patient patient) {
        super(patient);
        this.city = patient.city;
        this.age = patient.age;
        this.idCard = patient.idCard;
        this.phoneNumber = patient.phoneNumber;
        this.priorityQueue = patient.priorityQueue;
        this.appointmentList = new ArrayList<>(patient.appointmentList);
    }

    public Patient(Long id, String firstName, String lastName) {
        super(id,firstName,lastName);
    }

    public Patient(String firstName, String lastName) {
        super(firstName,lastName);
    }

    public Patient(Long id, String firstName, String lastName, List<Appointment> appointmentList) {
        super(id,firstName,lastName);
        this.appointmentList = appointmentList;
    }

    /**
     *
     * @return semi-anonymous string for admin purposes
     */
    public String privacyName(){
        StringBuilder stringBuilder = new StringBuilder();
        String pLastName= Strings.padEnd(Strings.nullToEmpty(getLastName()),3,' ').substring(0,1);
        String pPhone=Strings.padEnd(Strings.nullToEmpty(getPhoneNumber()),4,' ');
        pPhone=pPhone.substring(pPhone.length()-3);
        stringBuilder.append(getFirstName()).append(" ")
                .append(pLastName).append(". ").append(pPhone);
        return stringBuilder.toString();
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
