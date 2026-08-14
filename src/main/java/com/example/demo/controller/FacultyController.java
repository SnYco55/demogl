package com.example.demo.controller;

import com.example.demo.entity.FacultyEntity;
import com.example.demo.service.FacultyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/faculties")
public class FacultyController {

    private final FacultyService service;

    public FacultyController(FacultyService service) { this.service = service; }

    @GetMapping
    public List<FacultyEntity> getFaculties() { return service.getFaculties(); }

    @GetMapping("/{id}")
    public FacultyEntity getFaculty(@PathVariable String id) { return service.getFacultyById(id); }
}
