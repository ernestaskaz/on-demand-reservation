package com.bootcamp.ondemandreservation.controller;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.service.AppointmentService;
import com.bootcamp.ondemandreservation.service.DoctorService;
import com.bootcamp.ondemandreservation.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/web/")
public class DoctorWebController {
    static final Logger log= LoggerFactory.getLogger(DoctorWebController.class);

    public static final String DOCTOR_EDIT_TEMPLATE = "doctorDetailsEdit";
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

        return "doctorAllAppointmentsView";
    }

    @GetMapping("/doctor/today-appointments")
    String doctorTodayAppointments(Model model){
        Doctor doctor = doctorService.getLoggedInDoctor();
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", doctorService.getUpcomingAppointmentsForToday(doctor.getId()));

        return "doctorTodayAppointmentView";
    }

    @GetMapping("/doctor/edit")
    String editLoggedInPatient(Model model){
        model.addAttribute("errors", Collections.EMPTY_MAP);
        Doctor doctor = doctorService.getLoggedInDoctor();
        model.addAttribute("doctor",doctor);
        return DOCTOR_EDIT_TEMPLATE;
    }

    @GetMapping("/doctor/schedule")
    String getDoctorSchedule(Model model){
        Doctor doctor = doctorService.getLoggedInDoctor();
        model.addAttribute("doctor", doctor);
        model.addAttribute("schedules", doctor.getSchedulesList());

        return "doctorScheduleView";
    }

    @GetMapping("/doctor/generate")
    String generateDoctorAppointments(Model model){
        Doctor doctor = doctorService.getLoggedInDoctor();
        appointmentService.generateAppointmentsBySchedule(doctor.getId(), 15);
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", doctor.getAppointmentList());

        return "doctorAllAppointmentsView";
    }

    @RequestMapping("/doctor/appointments/cancel")
    String doctorAppointmentCancel(@RequestParam Long id, @RequestParam Long patientId, Model model){
        Doctor doctor = doctorService.getLoggedInDoctor();
        boolean canceled=false;
        try{
            canceled=appointmentService.cancelAppointment(appointmentService.getAppointmentById(id).getPatient().getId(), patientId);
        }catch(Throwable t){
            log.error("cancellation error ",t);
        }
        if(canceled) {
            model.addAttribute("reserveMsg", "Appointment canceled");
        }else{
            model.addAttribute("reserveMsg", "Appointment cancellation failed");
        }
        return doctorAllAppointments(model);//Not sure if this is good
    }




}
