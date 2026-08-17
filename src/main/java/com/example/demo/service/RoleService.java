package com.example.demo.service;

import com.example.demo.dto.role.RoleResponse;
import com.example.demo.entity.RoleEntity;
import com.example.demo.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoleService {

    private RoleRepository repository;

    public RoleService(RoleRepository repository) { this.repository = repository; }

    public List<RoleResponse> getRoles() {
        return repository.findAll()
                .stream()
                .map(role -> new RoleResponse(
                        role.getId(),
                        role.getName(),
                        role.getCreatedAt()
                ))
                .toList();
    }

    public RoleResponse getRoleById(Integer id) {
        RoleEntity role = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Role not found"
                        )
                );

        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getCreatedAt()
        );
    }
}
