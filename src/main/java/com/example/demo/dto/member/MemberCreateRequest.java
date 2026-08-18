package com.example.demo.dto.member;

import java.time.LocalDateTime;
import java.util.List;

public record MemberCreateRequest(
        String firstname,
        String lastname,
        LocalDateTime start,
        LocalDateTime end,
        List<String> serviceIds,
        List<Integer> roleIds
) {}