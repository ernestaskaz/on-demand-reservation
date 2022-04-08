package com.bootcamp.ondemandreservation.controller;


import com.bootcamp.ondemandreservation.model.Admin;
import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/web/")
@SessionAttributes({"admin","doctor"})
public class AdminWebController {
    public static final String DOCTOR_GET_ALL = "/admin/all-doctors";
    public static final String ADMIN_DETAILS_URL = "/admin/myDetails";
    public static final String ADMIN_EDIT_URL = "/admin/edit";
    public static final String APPOINTMENT_GET_ALL = "/admin/all-appointments";
    public static final String APPOINTMENT_GET_ALL_TODAY = "/admin/today-appointments";
    public static final String ADMIN_EDIT_TEMPLATE = "adminDetailsEdit";
    public static final String DOCTOR_CREATE_URL = "/doctor/create";
    public static final String DOCTOR_CREATE_TEMPLATE = "doctorCreate";
    public static final String ADMIN_CREATE_URL = "/admin/create";
    public static final String ADMIN_CREATE_TEMPLATE = "adminCreate";
    @Autowired
    private AdminService adminService;
    @Autowired
    private ODRUserService odrUserService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    PatientService patientService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private ODRPasswordEncoder odrPasswordEncoder;

    public AdminWebController(AdminService adminService, ODRUserService odrUserService, DoctorService doctorService, PatientService patientService, AppointmentService appointmentService, ODRPasswordEncoder odrPasswordEncoder) {
        this.adminService = adminService;
        this.odrUserService = odrUserService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.odrPasswordEncoder = odrPasswordEncoder;
    }


    @InitBinder("admin")
    public void setAllowedFieldsAdminEdit(WebDataBinder dataBinder) {
        // Disallow binding of sensitive fields - user can't override
        // values from the session
        dataBinder.setDisallowedFields("id", "email");
    }
    @InitBinder("doctor")
    public void setAllowedFieldsDoctorCreate(WebDataBinder dataBinder) {
        // Disallow binding of sensitive fields - user can't override
        // values from the session
        dataBinder.setDisallowedFields("id");
    }


    @GetMapping(DOCTOR_CREATE_URL)
    @PreAuthorize(Admin.ADMIN_ROLE)
    String createDoctor(Model model){
        model.addAttribute("errors", Collections.EMPTY_MAP);
        model.addAttribute("doctor",new Doctor());
        return DOCTOR_CREATE_TEMPLATE;
    }
    @PostMapping(DOCTOR_CREATE_URL)
    @PreAuthorize(Admin.ADMIN_ROLE)
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
    @PreAuthorize(Admin.ADMIN_ROLE)
    String createAdmin(Model model){
        model.addAttribute("errors", Collections.EMPTY_MAP);
        model.addAttribute("admin",new Admin());
        return ADMIN_CREATE_TEMPLATE;
    }
    @PostMapping(ADMIN_CREATE_URL)
    @PreAuthorize(Admin.ADMIN_ROLE)
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


    @GetMapping(DOCTOR_GET_ALL)
    @PreAuthorize(Admin.ADMIN_ROLE)
    String getAllDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "allDoctorsView";
    }

    @GetMapping(APPOINTMENT_GET_ALL)
    @PreAuthorize(Admin.ADMIN_ROLE)
    String getAllAppointments(Model model) {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        return "allAppointmentsView";
    }

    @GetMapping(APPOINTMENT_GET_ALL_TODAY)
    @PreAuthorize(Admin.ADMIN_ROLE)
    String getAllTodayAppointments(Model model) {
        List<Appointment> appointments = appointmentService.getTodaysAppointments();
        model.addAttribute("appointments", appointments);
        return "allAppointmentsTodayView";
    }


    @GetMapping("/admin/myDetails")
    @PreAuthorize(Admin.ADMIN_ROLE)
    String adminDetails(Model model){
        Admin admin = adminService.getLoggedInAdmin();
        model.addAttribute("admin", admin);
        return "adminAccountView";
    }

    @GetMapping(ADMIN_EDIT_URL)
    @PreAuthorize(Admin.ADMIN_ROLE)
    String editLoggedInAdmin(Model model){
        model.addAttribute("errors", Collections.EMPTY_MAP);
        Admin admin = adminService.getLoggedInAdmin();
        admin.blankPasswords();
        model.addAttribute("admin",admin);
        return ADMIN_EDIT_TEMPLATE;
    }

    @PostMapping(ADMIN_EDIT_URL)
    @PreAuthorize(Admin.ADMIN_ROLE)
    String editLoggedInAdmin(@ModelAttribute Admin admin, BindingResult result, Model model){
        //note matchPassword is true now.
        Map errors= adminService.validateAdmin(admin, true, true);
        model.addAttribute("admin", admin);
        model.addAttribute("errors", errors);
        if(errors.isEmpty()) {
            String currentPassword=adminService.getLoggedInAdmin().getPassword();
            if(odrPasswordEncoder.defaultPasswordEncoder().matches(admin.getPassword(),currentPassword)) {

                if(admin.getNewPassword()!=null&&!admin.getNewPassword().isBlank()){
                    //user wants to change password
                    admin.setPassword(admin.getNewPassword());//plain text
                    adminService.saveAdminAndPassword(admin);
                }else {
                    //User doesn't want to change password
                    admin.setPassword(currentPassword);//encrypted
                    adminService.saveAdmin(admin);
                }
                model.addAttribute("successMsg","Your data were updated successfully.");
            }else{
                errors.put("password","Incorrect password");
            }//else of if passwords match

        }//if validation errors empty
        admin.blankPasswords();
        return ADMIN_EDIT_TEMPLATE;
    }


}
