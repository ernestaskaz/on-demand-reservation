package com.bootcamp.ondemandreservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * pages that don't fit any other Controller,
 */
@Controller
public class CommonController {
    //Crude hacks to work around context path problem until I find real solution.
    @GetMapping("/web/")
    String eoot(Model model){
        return "index";
    }
    @GetMapping("/logoutSuccess")
    String logoutSuccess(Model model){
        return "logoutSuccess";
    }
}
