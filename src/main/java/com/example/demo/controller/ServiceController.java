package com.example.demo.controller;

import com.example.demo.entity.ServiceEntity;
import com.example.demo.service.ServiceService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService service;

    public ServiceController(ServiceService service) { this.service = service; }

    @GetMapping
    public List<ServiceEntity> getServices() { return service.getServices(); }
}
