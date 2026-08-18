package com.example.demo.controller;

import com.example.demo.dto.faculty.FacultyCreateRequest;
import com.example.demo.dto.faculty.FacultyResponse;
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
    public List<FacultyResponse> getFaculties() { return service.getFaculties(); }

    @GetMapping("/{id}")
    public FacultyResponse getFaculty(@PathVariable String id) { return service.getFacultyById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FacultyResponse createFaculty(@RequestBody FacultyCreateRequest request) {
        return service.createFaculty(request);
    }

    @PatchMapping("/{id}")
    public FacultyResponse updateFaculty(@PathVariable String id, @RequestBody FacultyPatchRequest request) {
        return service.updateFaculty(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFaculty(@PathVariable String id) {
        service.deleteFaculty(id);
    }
}
