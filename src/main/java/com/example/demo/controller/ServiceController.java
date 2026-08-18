package com.example.demo.controller;

import com.example.demo.dto.department.DepartmentResponse;
import com.example.demo.dto.faculty.FacultyResponse;
import com.example.demo.dto.member.MemberListResponse;
import com.example.demo.dto.service.ServiceCreateRequest;
import com.example.demo.dto.service.ServiceDetailsResponse;
import com.example.demo.dto.service.ServiceListResponse;
import com.example.demo.dto.service.ServicePatchRequest;
import com.example.demo.service.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService service;

    public ServiceController(ServiceService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServiceListResponse> getServices() {
        return service.getServices();
    }

    @GetMapping("/{id}")
    public ServiceDetailsResponse getServiceById(@PathVariable String id) {
        return service.getServiceById(id);
    }

    @GetMapping("/{id}/department")
    public DepartmentResponse getDepartmentByServiceId(@PathVariable String id) {
        return service.getDepartmentByServiceId(id);
    }

    @GetMapping("/{id}/faculty")
    public FacultyResponse getFacultyByServiceId(@PathVariable String id) {
        return service.getFacultyByServiceId(id);
    }

    @GetMapping("/{id}/director")
    public MemberListResponse getDirectorByServiceId(@PathVariable String id) {
        return service.getDirectorByServiceId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceDetailsResponse createService(
            @RequestBody ServiceCreateRequest request
    ) {
        return service.createService(request);
    }

    @PatchMapping("/{id}")
    public ServiceDetailsResponse updateService(
            @PathVariable String id,
            @RequestBody ServicePatchRequest request
    ) {
        return service.updateService(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(@PathVariable String id) {
        service.deleteService(id);
    }
}