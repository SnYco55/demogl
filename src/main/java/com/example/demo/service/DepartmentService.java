package com.example.demo.service;

import com.example.demo.dto.department.DepartmentDetailsResponse;
import com.example.demo.dto.department.DepartmentListResponse;
import com.example.demo.dto.faculty.FacultyDetailsResponse;
import com.example.demo.entity.DepartmentEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public List<DepartmentListResponse> getDepartments() {
        return repository.findAll()
                .stream()
                .map(department -> new DepartmentListResponse(
                        department.getId(),
                        department.getFaculty().getId(),
                        department.getCreatedAt()
                ))
                .toList();
    }

    public DepartmentDetailsResponse getDepartmentById(String id) {
        DepartmentEntity department = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Department not found"
                        )
                );

        return new DepartmentDetailsResponse(
                department.getId(),
                department.getFaculty().getId(),
                department.getCreatedAt()
        );
    }

    public FacultyDetailsResponse getFacultyByDepartmentId(String id) {
        DepartmentEntity department = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Faculty not found"
                        )
                );

        return new FacultyDetailsResponse(
                department.getFaculty().getId(),
                department.getFaculty().getName(),
                department.getFaculty().getCreatedAt()
        );
    }
}