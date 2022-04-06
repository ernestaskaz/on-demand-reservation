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
    /**
     * Validates the user
     * All doctor/patient/admin validations could call this
     * to get common fields validated.
     * @param user user to be validated
     * @param matchPassword if we should check for password and confirmPassword to match.
     * @return Map with fields (email, not getEmail) as keys and error messages as values
     */
    Map<String, String> validate(ODRUser user, boolean matchPassword, boolean forUpdate);
}
