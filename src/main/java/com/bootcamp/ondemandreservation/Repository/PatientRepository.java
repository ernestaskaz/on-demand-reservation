package com.bootcamp.ondemandreservation.Repository;

import com.bootcamp.ondemandreservation.Model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}
