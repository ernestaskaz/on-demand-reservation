package com.bootcamp.ondemandreservation.service;

import com.bootcamp.ondemandreservation.model.ODRUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface ODRUserService extends UserDetailsService {

    List<ODRUser> getAllODRUsers();
    ODRUser findODRUserById(Long id);
    ODRUser findODRUsersByEmail(String email);
    ODRUser getLoggedInODRUser();

}
