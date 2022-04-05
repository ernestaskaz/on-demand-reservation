package com.bootcamp.ondemandreservation.controller;

import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.service.PatientService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
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
}
