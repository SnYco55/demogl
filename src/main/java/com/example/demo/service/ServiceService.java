package com.example.demo.service;

import com.example.demo.dto.department.DepartmentResponse;
import com.example.demo.dto.faculty.FacultyResponse;
import com.example.demo.dto.member.MemberListResponse;
import com.example.demo.dto.service.ServiceCreateRequest;
import com.example.demo.dto.service.ServiceDetailsResponse;
import com.example.demo.dto.service.ServiceListResponse;
import com.example.demo.dto.service.ServicePatchRequest;
import com.example.demo.entity.DepartmentEntity;
import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.ServiceEntity;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ServiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServiceService {

    private final ServiceRepository repository;
    private final DepartmentRepository departmentRepository;
    private final MemberRepository memberRepository;

    public ServiceService(
            ServiceRepository repository,
            DepartmentRepository departmentRepository,
            MemberRepository memberRepository
    ) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
        this.memberRepository = memberRepository;
    }

    public List<ServiceListResponse> getServices() {
        return repository.findAll()
                .stream()
                .map(service -> new ServiceListResponse(
                        service.getId(),
                        service.getName(),
                        service.getDepartment().getId(),
                        service.getDirector().getId()
                ))
                .toList();
    }

    public ServiceDetailsResponse getServiceById(String id) {
        ServiceEntity service = findService(id);

        return toDetailsResponse(service);
    }

    public ServiceDetailsResponse createService(ServiceCreateRequest request) {

        String id = normalizeId(request.id());
        String name = normalizeName(request.name());
        String departmentId = normalizeDepartmentId(request.departmentId());

        if (repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Service id already exists"
            );
        }

        DepartmentEntity department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Department not found"
                ));

        MemberEntity director = findMember(request.directorId());

        ServiceEntity service = new ServiceEntity();

        service.setId(id);
        service.setName(name);
        service.setDepartment(department);
        service.setDirector(director);

        repository.save(service);

        return toDetailsResponse(service);
    }

    public ServiceDetailsResponse updateService(
            String id,
            ServicePatchRequest request
    ) {
        ServiceEntity service = findService(id);

        if (request.name() != null) {
            service.setName(normalizeName(request.name()));
        }

        if (request.departmentId() != null) {
            String departmentId = normalizeDepartmentId(
                    request.departmentId()
            );

            DepartmentEntity department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Department not found"
                    ));

            service.setDepartment(department);
        }

        if (request.directorId() != null) {
            MemberEntity director = findMember(request.directorId());
            service.setDirector(director);
        }

        repository.save(service);

        return toDetailsResponse(service);
    }

    public void deleteService(String id) {
        ServiceEntity service = findService(id);

        repository.delete(service);
    }

    public DepartmentResponse getDepartmentByServiceId(String id) {
        ServiceEntity service = findService(id);

        DepartmentEntity department = service.getDepartment();

        return new DepartmentResponse(
                department.getId(),
                department.getFaculty().getId()
        );
    }

    public FacultyResponse getFacultyByServiceId(String id) {
        ServiceEntity service = findService(id);

        var faculty = service.getDepartment().getFaculty();

        return new FacultyResponse(
                faculty.getId(),
                faculty.getName()
        );
    }

    public MemberListResponse getDirectorByServiceId(String id) {
        ServiceEntity service = findService(id);

        MemberEntity director = service.getDirector();

        return new MemberListResponse(
                director.getId(),
                director.getFirstname(),
                director.getLastname(),
                director.getStart(),
                director.getEnd()
        );
    }

    private ServiceEntity findService(String id) {
        if (id == null || id.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Service id is required"
            );
        }

        return repository.findById(id.trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Service not found"
                ));
    }

    private MemberEntity findMember(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Director id is required"
            );
        }

        return memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Director not found"
                ));
    }

    private String normalizeId(String value) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Service id is required"
            );
        }

        return value.trim();
    }

    private String normalizeName(String value) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Service name is required"
            );
        }

        return value.trim();
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

    private ServiceDetailsResponse toDetailsResponse(ServiceEntity service) {

        DepartmentEntity department = service.getDepartment();
        var faculty = department.getFaculty();
        MemberEntity director = service.getDirector();

        return new ServiceDetailsResponse(
                service.getId(),
                service.getName(),

                new DepartmentResponse(
                        department.getId(),
                        faculty.getId()
                ),

                new FacultyResponse(
                        faculty.getId(),
                        faculty.getName()
                ),

                new MemberListResponse(
                        director.getId(),
                        director.getFirstname(),
                        director.getLastname(),
                        director.getStart(),
                        director.getEnd()
                )
        );
    }
}