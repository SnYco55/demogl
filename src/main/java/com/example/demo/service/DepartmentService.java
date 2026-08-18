package com.example.demo.service;

import com.example.demo.dto.department.DepartmentCreateRequest;
import com.example.demo.dto.department.DepartmentPatchRequest;
import com.example.demo.dto.department.DepartmentResponse;
import com.example.demo.dto.faculty.FacultyResponse;
import com.example.demo.entity.DepartmentEntity;
import com.example.demo.entity.FacultyEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.demo.repository.FacultyRepository;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.ServiceRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DepartmentService {


    private final DepartmentRepository repository;
    private final FacultyRepository facultyRepository;
    private final ServiceRepository serviceRepository;

    public DepartmentService(DepartmentRepository repository, FacultyRepository facultyRepository, ServiceRepository serviceRepository) {
        this.repository = repository;
        this.facultyRepository = facultyRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<DepartmentResponse> getDepartments() {
        return repository.findAll()
                .stream()
                .map(department -> new DepartmentResponse(
                        department.getId(),
                        department.getFaculty().getId(),
                        department.getCreatedAt()
                ))
                .toList();
    }

    public DepartmentResponse getDepartmentById(String id) {
        DepartmentEntity department = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Department not found"
                        )
                );

        return new DepartmentResponse(
                department.getId(),
                department.getFaculty().getId(),
                department.getCreatedAt()
        );
    }

    public DepartmentResponse createDepartment(DepartmentCreateRequest request) {
        String id = normalizeDepartmentId(request.id());
        String facultyId = normalizeFacultyId(request.facultyId());

        if (repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Department id already exists"
            );
        }

        FacultyEntity faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Faculty not found"
                ));

        DepartmentEntity department = new DepartmentEntity();
        department.setId(id);
        department.setFaculty(faculty);

        repository.save(department);
        return new DepartmentResponse(
                department.getId(),
                department.getFaculty().getId(),
                department.getCreatedAt()
        );
    }

    public DepartmentResponse updateDepartment(String id, DepartmentPatchRequest request) {
        DepartmentEntity department = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Department not found"
                ));

        String facultyId = normalizeFacultyId(request.facultyId());
        FacultyEntity faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Faculty not found"
                ));

        department.setFaculty(faculty);
        repository.save(department);

        return new DepartmentResponse(
                department.getId(),
                department.getFaculty().getId(),
                department.getCreatedAt()
        );
    }

    public void deleteDepartment(String id) {
        DepartmentEntity department = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Department not found"
                ));

        long servicesCount = serviceRepository.countByDepartment_Id(department.getId());
        if (servicesCount > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Department cannot be deleted while it has services"
            );
        }

        repository.delete(department);
    }

    public FacultyResponse getFacultyByDepartmentId(String id) {
        DepartmentEntity department = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Department not found"
                        )
                );

        return new FacultyResponse(
                department.getFaculty().getId(),
                department.getFaculty().getName(),
                department.getFaculty().getCreatedAt()
        );
    }

    private String normalizeDepartmentId(String value) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Department id is required"
            );
        }

        return value.trim();
    }

    private String normalizeFacultyId(String value) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Department id already exists"
            );
        }

        return value.trim();
    }
}