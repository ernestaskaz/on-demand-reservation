package com.bootcamp.ondemandreservation.Repository;

import com.bootcamp.ondemandreservation.Model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
