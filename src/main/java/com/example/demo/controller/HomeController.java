package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "DemoGL API is running. Check http://localhost:8080/swagger-ui.html for API documentation.";
    }
}