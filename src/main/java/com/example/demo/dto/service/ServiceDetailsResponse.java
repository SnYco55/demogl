package com.example.demo.dto.service;

import com.example.demo.dto.department.DepartmentListResponse;
import com.example.demo.dto.faculty.FacultyListResponse;
import com.example.demo.dto.member.MemberListResponse;

import java.time.LocalDateTime;

public record ServiceDetailsResponse(
        String id,
        String name,
        DepartmentListResponse department,
        FacultyListResponse faculty,
        MemberListResponse director,
        LocalDateTime createdAt
) {}
