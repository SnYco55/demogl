package com.example.demo.dto.faculty;

import java.time.LocalDateTime;

public record FacultyListResponse(
        String id,
        String name,
        LocalDateTime createdAt
) {}
