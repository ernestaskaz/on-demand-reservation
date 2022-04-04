package com.bootcamp.ondemandreservation.Repository;

import com.bootcamp.ondemandreservation.Model.ODRUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ODRUserRepository extends JpaRepository<ODRUser,Long> {
    public Optional<ODRUser> findByEmail(String email);
}
