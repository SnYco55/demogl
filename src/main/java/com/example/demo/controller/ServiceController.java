package com.example.demo.controller;

import com.example.demo.dto.service.ServiceDetailsResponse;
import com.example.demo.dto.service.ServiceListResponse;
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
    public List<ServiceListResponse> getServices() { return service.getServices(); }

    @GetMapping("/{id}")
    public ServiceDetailsResponse getService(@PathVariable String id) { return service.getServiceById(id); }
}
