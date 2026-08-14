package com.example.demo.service;

import com.example.demo.entity.ServiceEntity;
import com.example.demo.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceService {

    private ServiceRepository repository;

    public ServiceService(ServiceRepository repository) {
        this.repository = repository;
    }

    public List<ServiceEntity> getServices() { return repository.findAll(); }

    public ServiceEntity getServiceById(String id) { return repository.findById(id).orElse(null); }
}
