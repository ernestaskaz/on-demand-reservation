package com.bootcamp.ondemandreservation.ServiceImplementation;

import com.bootcamp.ondemandreservation.Model.Admin;
import com.bootcamp.ondemandreservation.Repository.AdminRepository;
import com.bootcamp.ondemandreservation.Service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImplementation implements AdminService {
    private AdminRepository adminRepository;

    public AdminServiceImplementation(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin findAdminById(Long id) {
        return adminRepository.findById(id).get();
    }

    @Override
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    /*
     * does not update the ID itself
     */
    @Override
    public Admin updateAdmin(Long id, Admin admin) {
        admin.setId(id);
        return saveAdmin(admin);
    }
}
