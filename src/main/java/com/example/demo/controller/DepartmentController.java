package com.example.demo.controller;

import com.example.demo.dto.department.DepartmentDetailsResponse;
import com.example.demo.dto.department.DepartmentListResponse;
import com.example.demo.dto.faculty.FacultyDetailsResponse;
import com.example.demo.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) { this.service = service; }

    @GetMapping
    public List<DepartmentListResponse> getDepartments() { return service.getDepartments(); }

    @GetMapping("/{id}")
    public DepartmentDetailsResponse getDepartmentById(@PathVariable String id) { return service.getDepartmentById(id); }

    @GetMapping("/{id}/faculty")
    public FacultyDetailsResponse getFacultyByDepartmentId(@PathVariable String id) { return service.getFacultyByDepartmentId(id); }

}