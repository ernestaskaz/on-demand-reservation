package com.bootcamp.ondemandreservation;

import com.bootcamp.ondemandreservation.model.Admin;
import com.bootcamp.ondemandreservation.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Used for custom initialisation on startup.
 * Currently creates a default admin account if there is no admin in the DB.
 */
@Component
public class DataInit {
    private static final Logger log= LoggerFactory.getLogger(DataInit.class);
    @Autowired
    private AdminService adminService;

    public DataInit(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostConstruct
    public void init(){
        initUsers();
    }

    private void initUsers(){
        Admin initialAdmin=new Admin(null,"admin@default.com","admind","Admin","Default");

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Authentication authentication  = new UsernamePasswordAuthenticationToken(initialAdmin.getUsername(), initialAdmin.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //SecurityContextHolder.getContext().setAuthentication(initialAdmin);

        if(adminService.getAllAdmins().size()==0){
            log.warn("No admins in the database, creating default admin user "+initialAdmin.toString());
            initialAdmin=adminService.saveAdmin(initialAdmin);
            adminService.changePassword(initialAdmin.getId(), initialAdmin.getPassword());
        }
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
