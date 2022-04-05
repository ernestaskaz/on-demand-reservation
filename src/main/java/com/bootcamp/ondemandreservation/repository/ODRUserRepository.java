package com.bootcamp.ondemandreservation.repository;

import com.bootcamp.ondemandreservation.model.ODRUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ODRUserRepository extends JpaRepository<ODRUser,Long> {
    public Optional<ODRUser> findByEmail(String email);
}
