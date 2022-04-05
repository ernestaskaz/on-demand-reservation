package com.bootcamp.ondemandreservation.Repository;

import com.bootcamp.ondemandreservation.Model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    List<Appointment> findByIsAvailableTrueAndIsReservedFalse();

}
