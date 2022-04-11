package com.bootcamp.ondemandreservation.repository;

import com.bootcamp.ondemandreservation.model.Appointment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    List<Appointment> findByIsAvailableTrueAndIsReservedFalseAndAppointmentTimeIsAfter(LocalDateTime localDateTime);

    List<Appointment> findByDoctorId(Long doctorId, Sort sort);

    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId,
            LocalDateTime start,
            LocalDateTime end,Sort sort);
}
