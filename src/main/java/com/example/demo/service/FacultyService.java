package com.example.demo.service;

import com.example.demo.dto.faculty.FacultyDetailsResponse;
import com.example.demo.dto.faculty.FacultyListResponse;
import com.example.demo.entity.FacultyEntity;
import com.example.demo.repository.FacultyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository repository;

    public FacultyService(FacultyRepository repository) {
        this.repository = repository;
    }

    public List<FacultyListResponse> getFaculties() {
        return repository.findAll()
                .stream()
                .map(faculty -> new FacultyListResponse(
                        faculty.getId(),
                        faculty.getName(),
                        faculty.getCreatedAt()
                ))
                .toList();
    }

    public FacultyDetailsResponse getFacultyById(String id) {
        FacultyEntity faculty = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Faculty not found"
                        )
                );

        return new FacultyDetailsResponse(
                faculty.getId(),
                faculty.getName(),
                faculty.getCreatedAt()
        );
    }
}
