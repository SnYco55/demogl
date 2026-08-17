package com.example.demo.dto.service;

import java.time.LocalDateTime;

public record ServiceListResponse(
        String id,
        String name,
        String departmentId,
        Integer directorId,
        LocalDateTime createdAt
) {}
