package com.example.demo.dto.role;

import java.time.LocalDateTime;

public record RoleResponse(
        Integer id,
        String name,
        LocalDateTime createdAt
) {}
