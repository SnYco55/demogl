package com.example.demo.service;

import com.example.demo.entity.FacultyEntity;
import com.example.demo.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository repository;

    public FacultyService(FacultyRepository repository) {
        this.repository = repository;
    }

    public List<FacultyEntity> getFaculties() { return repository.findAll(); }
}
