package com.bootcamp.ondemandreservation.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isReserved = false;
    private boolean isAvailable = true;
    private boolean isConfirmed = false;
    private boolean wasAttended = false;
    private String comment = "";
    private double price;
    private LocalDateTime appointmentTime;

    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnoreProperties({"appointmentList", "schedulesList"})
    private Doctor doctor;
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnoreProperties({"appointmentList","schedulesList"})
    private Patient patient;

    public Appointment(){

    }

    public Appointment(Doctor doctor, Patient patient) {
        appointmentTime = LocalDateTime.now();
        this.doctor = doctor;
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public boolean isWasAttended() {
        return wasAttended;
    }

    public void setWasAttended(boolean wasAttended) {
        this.wasAttended = wasAttended;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void removeDoctor() {
        this.doctor = null;
    }

    public void removePatient() {
        this.patient = null;
    }
}
