package com.bootcamp.ondemandreservation.controller;

import com.bootcamp.ondemandreservation.model.Appointment;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.service.AppointmentService;
import com.bootcamp.ondemandreservation.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@SessionAttributes("patient")
public class PatientWebController {
    static final Logger log= LoggerFactory.getLogger(PatientWebController.class);
    //public static final String PATIENT_EDIT_URL = "/patient/edit";
    public static final String PATIENT_EDIT_TEMPLATE = "patientDetailsEdit";
    @Autowired
    private PatientService patientService;
    @Autowired
    private ODRPasswordEncoder odrPasswordEncoder;
    @Autowired
    private AppointmentService appointmentService;

    public PatientWebController(){}

    public PatientWebController(PatientService patientService, ODRPasswordEncoder odrPasswordEncoder, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.odrPasswordEncoder = odrPasswordEncoder;
        this.appointmentService = appointmentService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        // Disallow binding of sensitive fields - user can't override
        // values from the session
        dataBinder.setDisallowedFields("id","email");
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

    @PreAuthorize(Patient.PATIENT_ROLE)
    @GetMapping("/patient/myDetails")
    String patientDetails(Model model){
        Patient patient = patientService.getLoggedInPatient();
        model.addAttribute("patient", patient);
        return "patientAccountView";
    }

    @PreAuthorize(Patient.PATIENT_ROLE)
    @GetMapping("/patient/appointments")
    String patientAppointments(Model model){
        Patient patient = patientService.getLoggedInPatient();
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", patient.getAppointmentList());
        return "patientAppointmentsView";
    }


    @PreAuthorize(Patient.PATIENT_ROLE)
    @GetMapping("/patient/available-appointments")
    String patientAppointmentsAvailable(Model model){
        List<Appointment>  appointments = appointmentService.findAvailableAndNotReserved();
        model.addAttribute("appointments", appointments);

        return "patientAvailableAppointmentsView";
    }
    @PreAuthorize(Patient.PATIENT_ROLE)
    @RequestMapping("/patient/appointments/selfReserve")
    String patientAppointmentsReserve(@RequestParam Long id, Model model){
        Patient patient = patientService.getLoggedInPatient();
        boolean updated=false;
        try {
            appointmentService.reserveAppointment(patient.getId(), id);
            updated=true;
        }catch(Throwable t){
            log.error("reserve appointment:",t);
            model.addAttribute("reserveMsg","Appointment reservation failed.");//TODO be less informative to end user
        }
        if(updated)model.addAttribute("reserveMsg","Appointment reserved");
        return patientAppointmentsAvailable(model);//Not sure if this is good
    }

    @PreAuthorize(Patient.PATIENT_ROLE)
    @RequestMapping("/patient/appointments/cancel")
    String patientAppointmentsCancel(@RequestParam Long id, Model model){
        Patient patient = patientService.getLoggedInPatient();
        boolean canceled=false;
        try{
            canceled=appointmentService.cancelAppointment(patient.getId(), id);
        }catch(Throwable t){
            log.error("cancellation error ",t);
        }
        if(canceled) {
            model.addAttribute("reserveMsg", "Appointment canceled");
        }else{
            model.addAttribute("reserveMsg", "Appointment cancellation failed");
        }
        return patientAppointments(model);//Not sure if this is good
    }

    @PreAuthorize(Patient.PATIENT_ROLE)
    @GetMapping("/patient/edit")
    String editLoggedInPatient(Model model){
        model.addAttribute("errors", Collections.EMPTY_MAP);
        Patient patient=patientService.getLoggedInPatient();
        patient.blankPasswords();
        model.addAttribute("patient",patient);
        return PATIENT_EDIT_TEMPLATE;
    }
    @PreAuthorize(Patient.PATIENT_ROLE_SAME_ID)
    @PostMapping("/patient/edit")
    String editLoggedInPatient(@ModelAttribute Patient patient, BindingResult result, Model model){
        //note matchPassword is true now.
        Map errors=patientService.validatePatient(patient,true,true);
        model.addAttribute("patient", patient);
        model.addAttribute("errors", errors);
        if(errors.isEmpty()) {
            String currentPassword=patientService.getLoggedInPatient().getPassword();
            if(odrPasswordEncoder.defaultPasswordEncoder().matches(patient.getPassword(),currentPassword)) {

                if(patient.getNewPassword()!=null&&!patient.getNewPassword().isBlank()){
                    //user wants to change password
                    patient.setPassword(patient.getNewPassword());//plain text
                    patientService.savePatientAndPassword(patient);
                }else {
                    //User doesn't want to change password
                    patient.setPassword(currentPassword);//encrypted
                    patientService.savePatient(patient);
                }
                model.addAttribute("successMsg","Your data were updated successfully.");
            }else{
                errors.put("password","Incorrect password");
            }//else of if passwords match

        }//if validation errors empty
        patient.blankPasswords();
        return PATIENT_EDIT_TEMPLATE;
    }
}
