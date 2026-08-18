package com.example.demo.controller;

import com.example.demo.dto.role.RoleRequest;
import com.example.demo.dto.role.RoleResponse;
import com.example.demo.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) { this.service = service; }

    @GetMapping
    public List<RoleResponse> getRoles() { return service.getRoles(); }

    @GetMapping("/{id}")
    public RoleResponse getRoleById(@PathVariable Integer id) { return service.getRoleById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponse createRole(@RequestBody RoleRequest request) {
        return service.createRole(request);
    }

    @PatchMapping("/{id}")
    public RoleResponse updateRole(@PathVariable Integer id, @RequestBody RoleRequest request) {
        return service.updateRole(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable Integer id) {
        service.deleteRole(id);
    }

}
