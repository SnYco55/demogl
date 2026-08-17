package com.example.demo.dto.faculty;

import java.time.LocalDateTime;

public record FacultyDetailsResponse(
        String id,
        String name,
        LocalDateTime createdAt
) {}