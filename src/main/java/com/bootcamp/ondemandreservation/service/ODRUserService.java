package com.bootcamp.ondemandreservation.service;

import com.bootcamp.ondemandreservation.model.ODRUser;
import com.bootcamp.ondemandreservation.model.Patient;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;


public interface ODRUserService extends UserDetailsService {

    List<ODRUser> getAllODRUsers();
    ODRUser findODRUserById(Long id);
    ODRUser findODRUsersByEmail(String email);
    ODRUser getLoggedInODRUser();

    Map<String, String> validate(ODRUser user);
}
