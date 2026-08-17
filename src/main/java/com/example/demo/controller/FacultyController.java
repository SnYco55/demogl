package com.example.demo.controller;

import com.example.demo.dto.faculty.FacultyDetailsResponse;
import com.example.demo.dto.faculty.FacultyListResponse;
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
    public List<FacultyListResponse> getFaculties() { return service.getFaculties(); }

    @GetMapping("/{id}")
    public FacultyDetailsResponse getFaculty(@PathVariable String id) { return service.getFacultyById(id); }
}
