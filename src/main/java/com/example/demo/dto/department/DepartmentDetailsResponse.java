package com.example.demo.dto.department;

import java.time.LocalDateTime;

public record DepartmentDetailsResponse(
        String id,
        String facultyId,
        LocalDateTime createdAt
) {
}
