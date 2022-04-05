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

    @GetMapping("/patient/list")
    String getPatients(Model model){
        List<Patient>  patients=patientService.getAllPatients();
        model.addAttribute("patients", patients);
        return "allPatientsView";
    }
    @GetMapping("/patient/myDetails")
    String patientDetails(Model model){
        Patient patient = getLoggedInPatient();
        model.addAttribute("patient", patient);
        return "patientView";
    }

    /*@GetMapping("/patient/account")
    String patientDetails2(Model model){
        Patient patient = getLoggedInPatient();
        model.addAttribute("patient", patient);
        return "patientAccountView";
    }*/


    private Patient getLoggedInPatient() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id=null;
        if (principal instanceof ODRUser) {
            id = ((ODRUser)principal).getId();
        } else {
            throw new ODRUserNotFoundException();
        }
        Patient  patient=patientService.findPatientById(id);
        if(patient==null){
            throw new ODRUserNotFoundException();
        }
        return patient;
    }
}
