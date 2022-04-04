package com.bootcamp.ondemandreservation.Service;

import com.bootcamp.ondemandreservation.Model.ODRUser;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ODRUserService extends UserDetailsService {

    List<ODRUser> getAllODRUsers();
    ODRUser findODRUserById(Long id);
    ODRUser findODRUsersByEmail(String email);

}
