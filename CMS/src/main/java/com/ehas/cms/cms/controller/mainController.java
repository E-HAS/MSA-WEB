package com.ehas.cms.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cms")
public class mainController {
	
    @GetMapping("/main")
    public String main() {
        return "/cms/main";
    }
    @GetMapping("/monitoring")
    public String monitoring() {
        return "/cms/monitoring";
    }
    @GetMapping("/lotto")
    public String lotto() {
        return "/cms/lotto";
    }
}
