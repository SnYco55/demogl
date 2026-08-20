package com.example.demo.dto.service;

import com.example.demo.dto.department.DepartmentResponse;
import com.example.demo.dto.faculty.FacultyResponse;
import com.example.demo.dto.member.MemberListResponse;

public record ServiceDetailsResponse(
        String id,
        String name,
        DepartmentResponse department,
        FacultyResponse faculty,
        MemberListResponse director
) {}
