package com.example.demo.service;

import com.example.demo.entity.RoleEntity;
import com.example.demo.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private RoleRepository repository;

    public RoleService(RoleRepository repository) { this.repository = repository; }

    public List<RoleEntity> getRoles() { return repository.findAll(); }

    public RoleEntity getRoleById(Integer id) { return repository.findById(id).orElse(null); }
}
