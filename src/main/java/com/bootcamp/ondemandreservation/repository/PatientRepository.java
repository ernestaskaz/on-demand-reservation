package com.bootcamp.ondemandreservation.repository;

import com.bootcamp.ondemandreservation.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}
