package com.example.demo.service;

import com.example.demo.entity.DepartmentEntity;
import com.example.demo.entity.FacultyEntity;
import org.springframework.stereotype.Service;
import com.example.demo.repository.DepartmentRepository;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public List<DepartmentEntity> getDepartments() {
        return repository.findAll();
    }

    public DepartmentEntity getDepartmentById(String id) { return repository.findById(id).orElse(null); }

    public FacultyEntity getFacultyByDepartmentId(String id) {
        DepartmentEntity department = repository.findById(id).orElse(null);
        if (department != null) {
            return department.getFaculty();
        }
        return null;
    }
}