package com.bootcamp.ondemandreservation.controller;

import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/web/")
public class AdminWebController {
    public static final String DOCTOR_GET_ALL = "/admin/all-doctors";
    public static final String APPOINTMENT_GET_ALL = "/admin/all-appointments";
    public static final String APPOINTMENT_GET_ALL_TODAY = "/admin/today-appointments";
    public static final String DOCTOR_CREATE_URL = "/doctor/create";
    public static final String DOCTOR_CREATE_TEMPLATE = "doctorCreate";
    public static final String ADMIN_ROLE = "hasRole('ROLE_ADMIN')";
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

    public AdminWebController(AdminService adminService, ODRUserService odrUserService, DoctorService doctorService, PatientService patientService, AppointmentService appointmentService) {
        this.adminService = adminService;
        this.odrUserService = odrUserService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
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
        Map errors = doctorService.validateDoctor(doctor, true);
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

    @GetMapping(DOCTOR_GET_ALL)
    @PreAuthorize(ADMIN_ROLE)
    String getAllDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "allDoctorsView";
    }

    @GetMapping(APPOINTMENT_GET_ALL)
    @PreAuthorize(ADMIN_ROLE)
    String getAllAppointments(Model model) {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        return "allAppointmentsView";
    }

    @GetMapping(APPOINTMENT_GET_ALL_TODAY)
    @PreAuthorize(ADMIN_ROLE)
    String getAllTodayAppointments(Model model) {
        List<Appointment> appointments = appointmentService.getTodaysAppointments();
        model.addAttribute("appointments", appointments);
        return "todaysAllAppointments";
    }

}
