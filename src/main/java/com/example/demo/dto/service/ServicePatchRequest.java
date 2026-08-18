package com.example.demo.dto.service;

public record ServicePatchRequest(
        String name,
        String departmentId,
        Integer directorId
) {}