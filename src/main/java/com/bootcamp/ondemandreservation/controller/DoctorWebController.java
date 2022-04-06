package com.bootcamp.ondemandreservation.controller;
import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.service.AppointmentService;
import com.bootcamp.ondemandreservation.service.DoctorService;
import com.bootcamp.ondemandreservation.service.PatientService;
import com.bootcamp.ondemandreservation.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DoctorWebController {

    public static final String DOCTOR_EDIT_TEMPLATE = "doctorDetailsEdit";
    public static final String DOCTOR_ALL_APPOINTMENTS = "doctorDetailsEdit";
    @Autowired
    private ODRPasswordEncoder odrPasswordEncoder;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private ScheduleService scheduleService;

    public DoctorWebController(ODRPasswordEncoder odrPasswordEncoder, AppointmentService appointmentService, DoctorService doctorService, ScheduleService scheduleService) {
        this.odrPasswordEncoder = odrPasswordEncoder;
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.scheduleService = scheduleService;
    }



    @GetMapping("/doctor/myDetails")
    String patientDetails(Model model){
        Doctor doctor = doctorService.getLoggedInDoctor();
        model.addAttribute("doctor", doctor);
        return "doctorAccountView";
    }

    @GetMapping("/doctor/all-appointments")
    String doctorAllAppointments(Model model){
        Doctor doctor = doctorService.getLoggedInDoctor();
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", doctor.getAppointmentList());

        return "patientAppointmentsView";
    }

    @GetMapping("/doctor/today-appointments")
    String doctorTodayAppointments(Model model){
        Doctor doctor = doctorService.getLoggedInDoctor();
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", doctorService.getTodaysAppointments(doctor.getId()));

        return "doctorTodayAppointmentView";
    }

//    @GetMapping("/patient/edit")
//    String editLoggedInPatient(Model model){
//        model.addAttribute("errors", Collections.EMPTY_MAP);
//        Patient patient=patientService.getLoggedInPatient();
//        model.addAttribute("patient",patient);
//        return PATIENT_EDIT_TEMPLATE;
//    }



}
