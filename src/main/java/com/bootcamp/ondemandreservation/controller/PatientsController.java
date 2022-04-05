package com.bootcamp.ondemandreservation.controller;

import com.bootcamp.ondemandreservation.model.ODRUser;
import com.bootcamp.ondemandreservation.model.ODRUserNotFoundException;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.service.PatientService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/web/")
public class PatientsController {
    @Autowired
    private PatientService patientService;

    public PatientsController(){}

    public PatientsController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/patients")
    String getPatients(Model model){
        List<Patient>  patients=patientService.getAllPatients();
        model.addAttribute("patients", patients);
        return "allPatientsView";
    }
    @GetMapping("/patients/myDetails")
    String patientDetails(Model model){
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
        model.addAttribute("patient", patient);
        return "patientView";
    }
}
