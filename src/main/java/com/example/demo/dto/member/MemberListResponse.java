package com.example.demo.dto.member;

import java.time.LocalDateTime;

public record MemberListResponse(
        Integer id,
        String firstname,
        String lastname,
        LocalDateTime start,
        LocalDateTime end
) {}
