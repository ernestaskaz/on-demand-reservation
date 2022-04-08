package com.bootcamp.ondemandreservation.serviceimpl;

import com.bootcamp.ondemandreservation.model.Admin;
import com.bootcamp.ondemandreservation.model.ODRUser;
import com.bootcamp.ondemandreservation.model.ODRUserNotFoundException;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.repository.AdminRepository;
import com.bootcamp.ondemandreservation.service.AdminService;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.service.ODRUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize(Admin.ADMIN_ROLE)
    @Override
    public void changePassword(Long id, String plaintextPassword) {
        Admin theAdmin=findAdminById(id);
        theAdmin.setPassword(odrPasswordEncoder.defaultPasswordEncoder().encode(plaintextPassword));
        saveAdmin(theAdmin);
    }
    @PreAuthorize(Admin.ADMIN_ROLE)
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
    public Admin getLoggedInAdmin() {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long id=null;
            if (principal instanceof ODRUser) {
                id = ((ODRUser)principal).getId();
            } else {
                throw new ODRUserNotFoundException();
            }
            Admin admin = findAdminById(id);
            if(admin==null){
                throw new ODRUserNotFoundException();
            }
            return admin;
        }

    @PreAuthorize(Admin.ADMIN_ROLE)
    @Override
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    /*
     * does not update the ID itself
     */
    @PreAuthorize(Admin.ADMIN_ROLE)
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
    public Map<String, String> validateAdmin(Admin admin, boolean matchPassword,boolean forUpdate) {
        return odrUserService.validate(admin,matchPassword,forUpdate);
    }

    @PreAuthorize(Admin.ADMIN_ROLE)
    @Override
    public Admin saveAdminAndPassword(Admin admin) {
        admin.setPassword(odrPasswordEncoder.defaultPasswordEncoder()
                .encode(admin.getPassword()));
        return saveAdmin(admin);
    }
}
