package com.example.demo.controller;

import com.example.demo.dto.faculty.FacultyCreateRequest;
import com.example.demo.dto.faculty.FacultyDetailsResponse;
import com.example.demo.dto.faculty.FacultyListResponse;
import com.example.demo.dto.faculty.FacultyPatchRequest;
import com.example.demo.service.FacultyService;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FacultyDetailsResponse createFaculty(@RequestBody FacultyCreateRequest request) {
        return service.createFaculty(request);
    }

    @PatchMapping("/{id}")
    public FacultyDetailsResponse updateFaculty(@PathVariable String id, @RequestBody FacultyPatchRequest request) {
        return service.updateFaculty(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFaculty(@PathVariable String id) {
        service.deleteFaculty(id);
    }
}
