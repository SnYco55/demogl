package com.example.demo.service;

import com.example.demo.dto.faculty.FacultyCreateRequest;
import com.example.demo.dto.faculty.FacultyResponse;
import com.example.demo.dto.faculty.FacultyPatchRequest;
import com.example.demo.entity.FacultyEntity;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.FacultyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository repository;
    private final DepartmentRepository departmentRepository;

    public FacultyService(FacultyRepository repository, DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
    }

    public List<FacultyResponse> getFaculties() {
        return repository.findAll()
                .stream()
                .map(faculty -> new FacultyResponse(
                        faculty.getId(),
                        faculty.getName()
                ))
                .toList();
    }

    public FacultyResponse getFacultyById(String id) {
        FacultyEntity faculty = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Faculty not found"
                        )
                );

        return new FacultyResponse(
                faculty.getId(),
                faculty.getName()
        );
    }

    public FacultyResponse createFaculty(FacultyCreateRequest request) {
        String id = normalizeId(request.id());
        String name = normalizeName(request.name());

        if (repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Faculty id already exists"
            );
        }

        if (repository.existsByName(name)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Faculty name already exists"
            );
        }

        FacultyEntity faculty = new FacultyEntity();
        faculty.setId(id);
        faculty.setName(name);

        repository.save(faculty);
        return new FacultyResponse(
                faculty.getId(),
                faculty.getName()
        );
    }

    public FacultyResponse updateFaculty(String id, FacultyPatchRequest request) {
        FacultyEntity faculty = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Faculty not found"
                        )
                );

        String name = normalizeName(request.name());



        if (!name.equals(faculty.getName()) && repository.existsByName(name)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Faculty name already exists"
            );
        }

        faculty.setName(name);


        repository.save(faculty);
        return new FacultyResponse(
                faculty.getId(),
                faculty.getName()
        );
    }

    public void deleteFaculty(String id) {
        FacultyEntity faculty = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Faculty not found"
                        )
                );

        long departmentsCount = departmentRepository.countByFaculty_Id(faculty.getId());
        if (departmentsCount > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Faculty cannot be deleted while it has departments"
            );
        }

        repository.delete(faculty);
    }


    private String normalizeId(String value) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Faculty id is required"
            );
        }

        return value.trim();
    }

    private String normalizeName(String value) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Faculty name is required"
            );
        }

        return value.trim();
    }
}
