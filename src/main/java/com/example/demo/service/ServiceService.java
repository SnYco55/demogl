package com.example.demo.service;

import com.example.demo.dto.service.ServiceDetailsResponse;
import com.example.demo.dto.service.ServiceListResponse;
import com.example.demo.entity.ServiceEntity;
import com.example.demo.mapper.Mapper;
import com.example.demo.repository.ServiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServiceService {

    private ServiceRepository repository;
    private Mapper mapper;

    public ServiceService(ServiceRepository repository,  Mapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ServiceListResponse> getServices() {
        return repository.findAll()
                .stream()
                .map(service -> new ServiceListResponse(
                        service.getId(),
                        service.getName(),
                        service.getDepartmentId(),
                        service.getDirectorId(),
                        service.getCreatedAt()
                ))
                .toList();
    }

    public ServiceDetailsResponse getServiceById(String id) {
        ServiceEntity service = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Service not found"
                ));

        return mapper.toServiceDetailsResponse(service);
    }
}
