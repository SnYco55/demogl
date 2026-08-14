package com.example.demo.controller;

import com.example.demo.entity.DepartmentEntity;
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
    public List<DepartmentEntity> getDepartments() { return service.getDepartments(); }

    @GetMapping("/{id}")
    public DepartmentEntity getDepartmentById(@PathVariable String id) { return service.getDepartmentById(id); };

}