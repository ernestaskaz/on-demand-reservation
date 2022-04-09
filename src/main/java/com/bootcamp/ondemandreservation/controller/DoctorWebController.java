package com.bootcamp.ondemandreservation.controller;
import com.bootcamp.ondemandreservation.model.Admin;
import com.bootcamp.ondemandreservation.model.Doctor;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.model.Schedule;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import com.bootcamp.ondemandreservation.service.AppointmentService;
import com.bootcamp.ondemandreservation.service.DoctorService;
import com.bootcamp.ondemandreservation.service.ScheduleService;
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
import java.util.Map;

@Controller
@RequestMapping("/web/")
@PreAuthorize(Doctor.DOCTOR_ROLE)
@SessionAttributes("doctor")
public class DoctorWebController {
    static final Logger log= LoggerFactory.getLogger(DoctorWebController.class);

    public static final String DOCTOR_EDIT_TEMPLATE = "doctorDetailsEdit";
    public static final String SCHEDULE_EDIT_TEMPLATE = "scheduleEditView";
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

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        // Disallow binding of sensitive fields - user can't override
        // values from the session
        dataBinder.setDisallowedFields("id", "email");
    }
    @GetMapping("/doctor/myDetails")
    String doctorDetails(Model model){
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

//    @GetMapping("/doctor/schedule/edit")
//    String getSchedule(@RequestParam Long id, Model model){
//       // model.addAttribute("errors", Collections.EMPTY_MAP);
//        Schedule schedule = scheduleService.findScheduleById(id);
//        model.addAttribute("schedule", schedule);
//        return "schedule";
//    }


    @GetMapping("/doctor/schedule/edit")
    String editSchedule(@RequestParam Long id, Model model){
        // model.addAttribute("errors", Collections.EMPTY_MAP);
        Schedule schedule = scheduleService.findScheduleById(id);
        model.addAttribute("schedule", schedule);
        return SCHEDULE_EDIT_TEMPLATE;
    }

    @GetMapping("/doctor/schedule/delete")
    String deleteSchedule(@RequestParam Long id, Model model){
        scheduleService.deleteScheduleById(id);
        return getDoctorSchedule(model);
    }

    @PostMapping("/doctor/schedule/edit")
    String editSchedule(@RequestParam Long id, @ModelAttribute Schedule schedule, BindingResult result, Model model){
        model.addAttribute("schedule", schedule);
        scheduleService.updateSchedule(id, schedule);
        return SCHEDULE_EDIT_TEMPLATE;
    }


    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @GetMapping("/doctor/edit")
    String editLoggedInDoctor(Model model){
        model.addAttribute("errors", Collections.EMPTY_MAP);
        Doctor doctor = doctorService.getLoggedInDoctor();
        doctor.blankPasswords();
        model.addAttribute("doctor", doctor);
        return "doctorDetailsEdit";
    }





    @PostMapping("/doctor/edit")
    String editLoggedInDoctor(@ModelAttribute Doctor doctor, BindingResult result, Model model){
        //note matchPassword is true now.
        Map errors=doctorService.validateDoctor(doctor,true,true);
        model.addAttribute("doctor", doctor);
        model.addAttribute("errors", errors);
        if(errors.isEmpty()) {
            String currentPassword=doctorService.getLoggedInDoctor().getPassword();
            if(odrPasswordEncoder.defaultPasswordEncoder().matches(doctor.getPassword(),currentPassword)) {

                if(doctor.getNewPassword()!=null&&!doctor.getNewPassword().isBlank()){
                    //user wants to change password
                    doctor.setPassword(doctor.getNewPassword());//plain text
                    doctorService.saveDoctorAndPassword(doctor);
                }else {
                    //User doesn't want to change password
                    doctor.setPassword(currentPassword);//encrypted
                    doctorService.saveDoctor(doctor);
                }
                model.addAttribute("successMsg","Your data were updated successfully.");
            }else{
                errors.put("password","Incorrect password");
            }//else of if passwords match

        }//if validation errors empty
        doctor.blankPasswords();
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
