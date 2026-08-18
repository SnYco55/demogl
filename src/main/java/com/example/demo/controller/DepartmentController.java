package com.example.demo.controller;

import com.example.demo.dto.department.DepartmentCreateRequest;
import com.example.demo.dto.department.DepartmentPatchRequest;
import com.example.demo.dto.department.DepartmentResponse;
import com.example.demo.dto.faculty.FacultyResponse;
import com.example.demo.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) { this.service = service; }

    @GetMapping
    public List<DepartmentResponse> getDepartments() { return service.getDepartments(); }

    @GetMapping("/{id}")
    public DepartmentResponse getDepartmentById(@PathVariable String id) { return service.getDepartmentById(id); }

    @GetMapping("/{id}/faculty")
    public FacultyResponse getFacultyByDepartmentId(@PathVariable String id) { return service.getFacultyByDepartmentId(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentResponse createDepartment(@RequestBody DepartmentCreateRequest request) {
        return service.createDepartment(request);
    }

    @PatchMapping("/{id}")
    public DepartmentResponse updateDepartment(@PathVariable String id, @RequestBody DepartmentPatchRequest request) {
        return service.updateDepartment(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepartment(@PathVariable String id) {
        service.deleteDepartment(id);
    }

}