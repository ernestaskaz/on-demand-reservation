package com.bootcamp.ondemandreservation.controller;
import com.bootcamp.ondemandreservation.model.*;
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
@SessionAttributes({"doctor", "schedule", "appointment"})
public class DoctorWebController {
    static final Logger log= LoggerFactory.getLogger(DoctorWebController.class);

    public static final String DOCTOR_EDIT_TEMPLATE = "doctorDetailsEdit";
    public static final String SCHEDULE_EDIT_TEMPLATE = "scheduleEditView";
    public static final String SCHEDULE_CREATE_TEMPLATE = "scheduleCreateView";
    public static final String SCHEDULE_CREATE_URL = "/doctor/schedule/create";
    public static final String DOCTOR_PAST_APPOINTMENTS_URL = "/doctor/past-appointments";
    public static final String DOCTOR_PAST_APPOINTMENTS_TEMPLATE = "doctorPastAppointmentsView";
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

    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @GetMapping("/doctor/myDetails")
    String doctorDetails(Model model){
        Doctor doctor = doctorService.getLoggedInDoctor();
        model.addAttribute("doctor", doctor);
        return "doctorAccountView";
    }
    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @GetMapping("/doctor/all-appointments")
    String doctorAllAppointments(Model model){
        Doctor doctor = doctorService.getLoggedInDoctor();
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointmentService.getAllAppointmentsByDoctorId(doctor.getId()));

        return "doctorAllAppointmentsView";
    }
    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @GetMapping("/doctor/today-appointments")
    String doctorTodayAppointments(Model model){
        Doctor doctor = doctorService.getLoggedInDoctor();
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointmentService.getUpcomingAppointmentsForTodayByDoctorId(doctor.getId()));

        return "doctorTodayAppointmentView";
    }

    @GetMapping(DOCTOR_PAST_APPOINTMENTS_URL)
    String doctorPastAppointments(Model model){
        Doctor doctor = doctorService.getLoggedInDoctor();
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointmentService.getPastAppointmentsByDoctorId(doctor.getId()));

        return "doctorPastAppointmentsView";
    }


    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @GetMapping("/doctor/schedule/edit")
    String editSchedule(@RequestParam Long id, Model model){
        model.addAttribute("errors", Collections.EMPTY_MAP);
        Schedule schedule = scheduleService.findScheduleById(id);
        model.addAttribute("schedule", schedule);
        return SCHEDULE_EDIT_TEMPLATE;
    }

    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @PostMapping("/doctor/schedule/edit")
    String editSchedule(@RequestParam Long id, @ModelAttribute Schedule schedule, BindingResult result, Model model){
        Map<String, String> errors = scheduleService.validateSchedule(schedule);
        model.addAttribute("schedule", schedule);
        model.addAttribute("errors", errors);

        if(errors.isEmpty()) {
            scheduleService.updateSchedule(id, schedule);
            model.addAttribute("successMsg","Your data was updated successfully.");
        }
        return SCHEDULE_EDIT_TEMPLATE;
    }

    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @GetMapping(SCHEDULE_CREATE_URL)
    String createSchedule(Model model){
        model.addAttribute("errors", Collections.EMPTY_MAP);
        model.addAttribute("schedule",new Schedule());
        return SCHEDULE_CREATE_TEMPLATE;
    }

    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @PostMapping(SCHEDULE_CREATE_URL)
    String createSchedule(@ModelAttribute Schedule schedule, @SessionAttribute Doctor doctor, Model model) {
        Map<String, String> errors = scheduleService.validateSchedule(schedule);
        model.addAttribute("schedule", schedule);
        model.addAttribute("doctor", doctor);
        model.addAttribute("errors", errors);
        if (errors.isEmpty()) {
            schedule.setDoctor(doctor);
            schedule = scheduleService.saveSchedule(schedule);
            model.addAttribute("successMsg", String.format( "Schedule %s from %s to %s created with ID %d",
                    schedule.getDayOfWeek(),
                    schedule.getStartHour(),
                    schedule.getEndHour(),
                    schedule.getId()));
        }
        return SCHEDULE_CREATE_TEMPLATE;
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




    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @PostMapping("/doctor/edit")
    String editLoggedInDoctor(@ModelAttribute Doctor doctor, BindingResult result, Model model){
        //note matchPassword is true now.
        System.out.println(doctor.getId());
        Map errors=doctorService.validateDoctor(doctor,true,true);
        model.addAttribute("doctor", doctor);
        model.addAttribute("errors", errors);
        if(errors.isEmpty()) {
            String currentPassword=doctorService.getLoggedInDoctor().getPassword();
            if(odrPasswordEncoder.defaultPasswordEncoder().matches(doctor.getPassword(),currentPassword)) {

                if(doctor.getNewPassword()!=null&&!doctor.getNewPassword().isBlank()){
                    //user wants to change password
                    doctor.setPassword(doctor.getNewPassword());//plain text
                    doctorService.saveDoctorAndPassword(doctor, false);
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

    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @GetMapping("/doctor/schedule")
    String getDoctorSchedule(Model model){
        Doctor doctor = doctorService.getLoggedInDoctor();
        model.addAttribute("doctor", doctor);
        model.addAttribute("schedules", doctor.getSchedulesList());

        return "doctorScheduleView";
    }

    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @GetMapping("/doctor/generate")
    String generateDoctorAppointments(Model model){
        Doctor doctor = doctorService.getLoggedInDoctor();
        appointmentService.generateAppointmentsBySchedule(doctor.getId(), 14);
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", doctor.getAppointmentList());

        return doctorAllAppointments(model);
    }

    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @RequestMapping("/doctor/past-appointments/was-attended")
    String wasAttendedAppointment(@RequestParam Long id, Model model){
        appointmentService.setAppointmentWasAttended(id);
        return doctorPastAppointments(model);
    }

    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @RequestMapping("/doctor/appointments/avail")
    String doctorAppointmentAvail(@RequestParam Long id, @RequestParam String from, Model model){
        boolean avail=false;// it's the same code as for cancellation, but it does a different thing.
        try{
            avail=appointmentService.flipAppointmentAvailable(id);
        }catch(Throwable t){
            log.error("cancellation error ",t);
        }
        if(avail) {
            model.addAttribute("reserveMsg", "Appointment marked as available");
        }else{
            model.addAttribute("reserveMsg", "Appointment marked as available");
        }
        if("today".equals(from)) //"literal".equals(variable)
            // is preferred as you can't call .equals() on null variables,
            // but you always can on literals.
            return doctorTodayAppointments(model);
        else if("all".equals(from)){
            return doctorAllAppointments(model);
        }else
            return doctorAllAppointments(model);//falback if nothing passed/error
    }


    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @RequestMapping("/doctor/appointments/cancel")
    String doctorAppointmentCancel(@RequestParam Long id, @RequestParam Long patientId,@RequestParam String from, Model model){
        boolean canceled=false;
        try{
            canceled=appointmentService.cancelAppointment(patientId,id);
        }catch(Throwable t){
            log.error("cancellation error ",t);
        }
        if(canceled) {
            model.addAttribute("reserveMsg", "Appointment canceled");
        }else{
            model.addAttribute("reserveMsg", "Appointment cancellation failed");
        }
        if("today".equals(from)) //"literal".equals(variable)
                                 // is preferred as you can't call .equals() on null variables,
                                 // but you always can on literals.
            return doctorTodayAppointments(model);
        else if("all".equals(from)){
            return doctorAllAppointments(model);
        }else
            return doctorAllAppointments(model);//falback if nothing passed/error
    }
    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @GetMapping("/doctor/schedule/delete")
    String deleteSchedule(@RequestParam Long id, Model model){
        scheduleService.deleteScheduleById(id);
        return getDoctorSchedule(model);
    }

    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @GetMapping("/appointment/edit")
    String editAppointment(@RequestParam Long id, @RequestParam String from, Model model){
        Appointment appointment = appointmentService.getAppointmentById(id);
        model.addAttribute("appointment", appointment);
        model.addAttribute("from", from);
        return "appointmentEditView";
    }

    @PreAuthorize(Doctor.DOCTOR_ROLE)
    @PostMapping("/appointment/edit")
    String editAppointment(@RequestParam Long id, @ModelAttribute String from, @ModelAttribute Appointment appointment, BindingResult result, Model model){
        appointmentService.updateAppointment(id, appointment);
        if("past".equals(from)){
            return doctorPastAppointments(model);
        }else if("all".equals(from)){
            return doctorAllAppointments(model);
        }
        //fallback
        model.addAttribute("appointment", appointment);
        model.addAttribute("successMsg","Appointment was updated successfully.");
        return "appointmentEditView";
    }





}
