package com.example.demo.dto.service;

public record ServiceListResponse(
        String id,
        String name,
        String departmentId,
        Integer directorId
) {}
