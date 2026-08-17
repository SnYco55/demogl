package com.example.demo.controller;

import com.example.demo.dto.role.RoleResponse;
import com.example.demo.entity.RoleEntity;
import com.example.demo.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/roles")
public class RoleController {

    private RoleService service;

    public RoleController(RoleService service) { this.service = service; }

    @GetMapping
    public List<RoleResponse> getRoles() { return service.getRoles(); }

    @GetMapping("/{id}")
    public RoleResponse getRoleById(@PathVariable Integer id) { return service.getRoleById(id); }

}
