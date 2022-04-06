package com.bootcamp.ondemandreservation.controller;

import com.bootcamp.ondemandreservation.model.ODRUser;
import com.bootcamp.ondemandreservation.model.ODRUserNotFoundException;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/web/")
public class PatientWebController {
    @Autowired
    private PatientService patientService;

    public PatientWebController(){}

    public PatientWebController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/patient/all-patients")
    String getPatients(Model model){
        List<Patient>  patients=patientService.getAllPatients();
        model.addAttribute("patients", patients);
        return "allPatientsView";
    }

    @GetMapping("/patient/list")
    String getPatient(Model model){
        List<Patient>  patients=patientService.getAllPatients();
        model.addAttribute("patients", patients);
        return "patientView";
    }

    @GetMapping("/patient/myDetails")
    String patientDetails(Model model){
        Patient patient = patientService.getLoggedInPatient();
        model.addAttribute("patient", patient);
        return "patientAccountView";
    }

    @GetMapping("/patient/appointments")
    String patientAppointments(Model model){
        Patient patient = patientService.getLoggedInPatient();
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", patient.getAppointmentList());
        return "patientAppointmentsView";
    }


    @GetMapping("/patient/available-appointments")
    String patientAppointmentsAvailable(Model model){
        return "patientAvailableAppointmentsView";
    }

//    @GetMapping("/patient/myAppointments")
//    String patientAppoimntments(Model model){
//        Patient patient = patientService.getLoggedInPatient();
//        model.addAttribute("patient", patient);
//        model.addAttribute("appointments", patient.getAppointmentList());
//        return "patientAppointmentView";
//    }
    /*@GetMapping("/patient/account")
    String patientDetails2(Model model){
        Patient patient = getLoggedInPatient();
        model.addAttribute("patient", patient);
        return "patientAccountView";
    }*/



}
