package com.bootcamp.ondemandreservation.ServiceImplementation;

import com.bootcamp.ondemandreservation.Model.Admin;
import com.bootcamp.ondemandreservation.Repository.AdminRepository;
import com.bootcamp.ondemandreservation.Service.AdminService;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImplementation implements AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ODRPasswordEncoder odrPasswordEncoder;

    public AdminServiceImplementation(){}

    public AdminServiceImplementation(AdminRepository adminRepository, ODRPasswordEncoder odrPasswordEncoder) {
        this.adminRepository = adminRepository;
        this.odrPasswordEncoder = odrPasswordEncoder;
    }

    public AdminServiceImplementation(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public void changePassword(Long id, String plaintextPassword) {
        Admin theAdmin=findAdminById(id);
        theAdmin.setPassword(odrPasswordEncoder.defaultPasswordEncoder().encode(plaintextPassword));
        saveAdmin(theAdmin);
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
