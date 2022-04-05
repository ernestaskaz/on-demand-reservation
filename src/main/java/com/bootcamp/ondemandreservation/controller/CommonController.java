package com.bootcamp.ondemandreservation.controller;

import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Map;

/**
 * pages that don't fit any other Controller,
 */
@Controller
public class CommonController {
    @Autowired
    private PatientService patientService;
    public CommonController(){}
    public CommonController(PatientService patientService){
        this.patientService=patientService;
    }

    //Crude hacks to work around context path problem until I find real solution.
    @GetMapping("/web/")
    String root(Model model){
        return "index";
    }

    @GetMapping("/logoutSuccess")
    String logoutSuccess(Model model){
        return "logoutSuccess";
    }

    @GetMapping("/register")
    String registrationForm(Model model){
        model.addAttribute("errors", Collections.EMPTY_MAP);
        model.addAttribute("patient",new Patient());
        return "register";
    }

    @PostMapping("/register")
    String registrationForm(@ModelAttribute Patient patient, Model model){
        Map errors=patientService.validatePatient(patient);
        if(!errors.isEmpty()) {
            model.addAttribute("patient", patient);
            model.addAttribute("errors", errors);
            return "register";
        }else{
            patient.setId(null);
            patientService.savePatient(patient);
            return "registerSuccess";
        }
    }
}
