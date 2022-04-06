package com.bootcamp.ondemandreservation.controller;

import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.service.AppointmentService;
import com.bootcamp.ondemandreservation.service.PatientService;
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
public class PatientWebController {
    //public static final String PATIENT_EDIT_URL = "/patient/edit";
    public static final String PATIENT_EDIT_TEMPLATE = "patientDetailsEdit";
    @Autowired
    private PatientService patientService;
    @Autowired
    private AppointmentService appointmentService;

    public PatientWebController(){}

    public PatientWebController(PatientService patientService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
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
        List<Appointment>  appointments = appointmentService.findAvailableAndNotReserved();
        model.addAttribute("appointments", appointments);

        return "patientAvailableAppointmentsView";
    }

    @GetMapping("/patient/edit")
    String editLoggedInPatient(Model model){
        model.addAttribute("errors", Collections.EMPTY_MAP);
        model.addAttribute("patient",patientService.getLoggedInPatient());
        return PATIENT_EDIT_TEMPLATE;
    }
    @PostMapping("/patient/edit")
    String editLoggedInPatient(@ModelAttribute Patient patient, Model model){
        //TODO Add change password functionality
        Map errors=patientService.validatePatient(patient,false);
        model.addAttribute("patient", patient);
        model.addAttribute("errors", errors);
        if(errors.isEmpty()) {
            patientService.savePatientAndPassword(patient);
            model.addAttribute("successMsg","Your data were updated successfully.");
        }
        return PATIENT_EDIT_TEMPLATE;
    }
}
