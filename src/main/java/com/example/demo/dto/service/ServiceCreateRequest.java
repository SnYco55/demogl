package com.example.demo.dto.service;

public record ServiceCreateRequest(
        String id,
        String name,
        String departmentId,
        Integer directorId
) {}