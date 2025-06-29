package com.ehas.cms.cms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class homeController {
	
    @GetMapping("/")
    public String index() {
        return "/cms/main";
    }
}
