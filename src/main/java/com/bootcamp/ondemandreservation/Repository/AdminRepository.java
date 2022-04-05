package com.bootcamp.ondemandreservation.Repository;

import com.bootcamp.ondemandreservation.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
}
