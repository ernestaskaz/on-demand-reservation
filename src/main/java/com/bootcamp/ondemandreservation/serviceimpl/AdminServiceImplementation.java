package com.bootcamp.ondemandreservation.serviceimpl;

import com.bootcamp.ondemandreservation.model.Admin;
import com.bootcamp.ondemandreservation.repository.AdminRepository;
import com.bootcamp.ondemandreservation.service.AdminService;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.service.ODRUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImplementation implements AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ODRPasswordEncoder odrPasswordEncoder;
    @Autowired
    private ODRUserService odrUserService;

    public AdminServiceImplementation(){}

    public AdminServiceImplementation(AdminRepository adminRepository, ODRPasswordEncoder odrPasswordEncoder, ODRUserService odrUserService) {
        this.adminRepository = adminRepository;
        this.odrPasswordEncoder = odrPasswordEncoder;
        this.odrUserService = odrUserService;
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

    /**
     * Validates the admin
     * currently just a wrapper around ODRUser.validate()'
     * @param admin         Admin to be validated
     * @param matchPassword if we should check for password and confirmPassword to match.
     * @return Map with fields (email, not getEmail) as keys and error messages as values
     */
    @Override
    public Map<String, String> validateAdmin(Admin admin, boolean matchPassword) {
        return odrUserService.validate(admin,matchPassword);
    }

    @Override
    public Admin saveAdminAndPassword(Admin admin) {
        admin.setPassword(odrPasswordEncoder.defaultPasswordEncoder()
                .encode(admin.getPassword()));
        return saveAdmin(admin);
    }
}
