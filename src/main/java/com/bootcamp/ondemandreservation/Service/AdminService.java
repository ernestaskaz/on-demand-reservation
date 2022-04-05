package com.bootcamp.ondemandreservation.Service;

import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Admin;

import java.util.List;

public interface AdminService {
    Admin saveAdmin(Admin admin);
    List<Admin> getAllAdmins();
    Admin findAdminById(Long id);
    void deleteAdmin(Long id);
    Admin updateAdmin(Long id, Admin admin);
}
