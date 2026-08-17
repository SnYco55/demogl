package com.example.demo.dto.member;

import com.example.demo.dto.role.RoleResponse;
import com.example.demo.dto.service.ServiceDetailsResponse;

import java.time.LocalDateTime;
import java.util.List;

public record MemberDetailsResponse(
        Integer id,
        String firstname,
        String lastname,
        LocalDateTime start,
        LocalDateTime end,
        List<ServiceDetailsResponse> services,
        List<RoleResponse> roles,
        LocalDateTime createdAt
) {}
