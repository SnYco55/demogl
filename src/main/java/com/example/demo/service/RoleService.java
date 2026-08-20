package com.example.demo.service;

import com.example.demo.dto.role.RoleRequest;
import com.example.demo.dto.role.RoleResponse;
import com.example.demo.entity.RoleEntity;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoleService {


    private final RoleRepository repository;
    private final MemberRepository memberRepository;

    public RoleService(RoleRepository repository, MemberRepository memberRepository) {
        this.repository = repository;
        this.memberRepository = memberRepository;
    }

    public List<RoleResponse> getRoles() {
        return repository.findAll()
                .stream()
                .map(role -> new RoleResponse(
                        role.getId(),
                        role.getName()
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
                role.getName()
        );
    }

    public RoleResponse createRole(RoleRequest request) {
        String name = normalizeName(request.name());

        if (repository.existsByName(name)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Role name already exists"
            );
        }

        RoleEntity role = new RoleEntity();
        role.setName(name);

        repository.save(role);
        return new RoleResponse(
                role.getId(),
                role.getName()
        );
    }

    public RoleResponse updateRole(Integer id, RoleRequest request) {
        RoleEntity role = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Role not found"
                ));

        String name = normalizeName(request.name());
        if (!name.equals(role.getName()) && repository.existsByName(name)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Role name already exists"
            );
        }

        role.setName(name);
        repository.save(role);

        return new RoleResponse(
                role.getId(),
                role.getName()
        );
    }

    public void deleteRole(Integer id) {
        RoleEntity role = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Role not found"
                ));

        long membersCount = memberRepository.countByRoles_Id(role.getId());
        if (membersCount > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Role cannot be deleted while it has members"
            );
        }

        repository.delete(role);
    }

    private String normalizeName(String value) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Role name is required"
            );
        }

        return value.trim();
    }
}
