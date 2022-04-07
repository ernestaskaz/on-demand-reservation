package com.bootcamp.ondemandreservation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.DayOfWeek;

@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    DayOfWeek dayOfWeek;
    int startHour;
    int endHour;
    int lunchTime;
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    @ManyToOne
    @JsonIgnoreProperties({"schedulesList", "appointmentList"})
    private Doctor doctor;

    public Schedule() {

    }

    public Schedule(DayOfWeek dayOfWeek, int startHour, int endHour) {
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public Schedule(Long id, DayOfWeek dayOfWeek, int startHour, int endHour, int lunchTime) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
        this.lunchTime = lunchTime;
    }

    public Schedule(DayOfWeek dayOfWeek, int startHour, int endHour, int lunchTime) {
        this.dayOfWeek = dayOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
        this.lunchTime = lunchTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getLunchTime() {
        return lunchTime;
    }

    public void setLunchTime(int lunchTime) {
        this.lunchTime = lunchTime;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void removeDoctor() {
        this.doctor = null;
    }
}
