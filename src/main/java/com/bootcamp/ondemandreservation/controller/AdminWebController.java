package com.bootcamp.ondemandreservation.controller;

import com.bootcamp.ondemandreservation.model.Admin;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.service.AdminService;
import com.bootcamp.ondemandreservation.service.DoctorService;
import com.bootcamp.ondemandreservation.service.ODRUserService;
import com.bootcamp.ondemandreservation.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/web/")
public class AdminWebController {
    public static final String DOCTOR_CREATE_URL = "/doctor/create";
    public static final String DOCTOR_CREATE_TEMPLATE = "doctorCreate";
    public static final String ADMIN_CREATE_URL = "/admin/create";
    public static final String ADMIN_CREATE_TEMPLATE = "adminCreate";
    public static final String ADMIN_ROLE = "hasRole('ROLE_ADMIN')";
    @Autowired
    private AdminService adminService;
    @Autowired
    private ODRUserService odrUserService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    PatientService patientService;

    public AdminWebController(AdminService adminService, ODRUserService odrUserService, DoctorService doctorService, PatientService patientService) {
        this.adminService = adminService;
        this.odrUserService = odrUserService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }
    @GetMapping(DOCTOR_CREATE_URL)
    @PreAuthorize(ADMIN_ROLE)
    String createDoctor(Model model){
        model.addAttribute("errors", Collections.EMPTY_MAP);
        model.addAttribute("doctor",new Doctor());
        return DOCTOR_CREATE_TEMPLATE;
    }
    @PostMapping(DOCTOR_CREATE_URL)
    @PreAuthorize(ADMIN_ROLE)
    String createDoctor(@ModelAttribute Doctor doctor, Model model) {
        Map errors = doctorService.validateDoctor(doctor, true,false);
        model.addAttribute("doctor", doctor);
        model.addAttribute("errors", errors);
        if (errors.isEmpty()) {
            doctor.setId(null);
            doctor=doctorService.saveDoctorAndPassword(doctor);
            doctor.setPassword("");
            doctor.setConfirmPassword("");
            model.addAttribute("successMsg", String.format( "Doctor %s %s <%s> created with ID %d",
                                                                            doctor.getFirstName(),
                                                                            doctor.getLastName(),
                                                                            doctor.getUsername(),
                                                                            doctor.getId()));
        }
        return DOCTOR_CREATE_TEMPLATE;
    }

    @GetMapping(ADMIN_CREATE_URL)
    @PreAuthorize(ADMIN_ROLE)
    String createAdmin(Model model){
        model.addAttribute("errors", Collections.EMPTY_MAP);
        model.addAttribute("admin",new Admin());
        return ADMIN_CREATE_TEMPLATE;
    }
    @PostMapping(ADMIN_CREATE_URL)
    @PreAuthorize(ADMIN_ROLE)
    String createAdmin(@ModelAttribute Admin admin, Model model) {
        Map errors = adminService.validateAdmin(admin, true,false);
        model.addAttribute("admin", admin);
        model.addAttribute("errors", errors);
        if (errors.isEmpty()) {
            admin.setId(null);
            admin=adminService.saveAdminAndPassword(admin);
            admin.setPassword("");
            admin.setConfirmPassword("");
            model.addAttribute("successMsg", String.format( "Administrator %s %s <%s> created with ID %d",
                    admin.getFirstName(),
                    admin.getLastName(),
                    admin.getUsername(),
                    admin.getId()));
        }
        return ADMIN_CREATE_TEMPLATE;
    }


}
