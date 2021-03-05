package com.tungstun.swagger.presentation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerController {
    @RequestMapping("/swagger")
    public String home() {
        return "redirect:/swagger-ui/index.html";
    }
}
