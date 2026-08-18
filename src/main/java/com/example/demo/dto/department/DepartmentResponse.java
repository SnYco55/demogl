package com.example.demo.dto.department;

import java.time.LocalDateTime;

public record DepartmentResponse(
        String id,
        String facultyId,
        LocalDateTime createdAt
) {}
