package com.bootcamp.ondemandreservation.service;

import com.bootcamp.ondemandreservation.model.Admin;
import com.bootcamp.ondemandreservation.model.Doctor;

import java.util.List;
import java.util.Map;

public interface AdminService {
    void changePassword(Long id, String plaintextPassword);
    Admin saveAdmin(Admin admin);
    List<Admin> getAllAdmins();
    Admin findAdminById(Long id);
    void deleteAdmin(Long id);
    Admin updateAdmin(Long id, Admin admin);
    /**
     * Validates the admin
     * @param admin Admin to be validated
     * @param matchPassword if we should check for password and confirmPassword to match.
     * @return Map with fields (email, not getEmail) as keys and error messages as values
     */
    Map<String, String> validateAdmin(Admin admin, boolean matchPassword);
}
