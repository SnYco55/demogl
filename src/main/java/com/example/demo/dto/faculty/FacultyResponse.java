package com.example.demo.dto.faculty;

import java.time.LocalDateTime;

public record FacultyResponse(
        String id,
        String name,
        LocalDateTime createdAt
) {}
