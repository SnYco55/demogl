package com.example.demo.controller;

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
    public List<RoleEntity> getRoles() { return service.getRoles(); }

    @GetMapping("/{id}")
    public RoleEntity getRoleById(@PathVariable Integer id) { return service.getRoleById(id); }

}
