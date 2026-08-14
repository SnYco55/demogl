package com.example.demo.controller;

import com.example.demo.entity.ServiceEntity;
import com.example.demo.service.ServiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService service;

    public ServiceController(ServiceService service) { this.service = service; }

    @GetMapping
    public List<ServiceEntity> getServices() { return service.getServices(); }

    @GetMapping("/{id}")
    public ServiceEntity getService(@PathVariable String id) { return service.getServiceById(id); }
}
