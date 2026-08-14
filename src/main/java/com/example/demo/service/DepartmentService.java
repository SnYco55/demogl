package com.example.demo.service;

import com.example.demo.entity.DepartmentEntity;
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
}