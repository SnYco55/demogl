package com.example.demo.controller;

import com.example.demo.dto.member.MemberDetailsResponse;
import com.example.demo.dto.member.MemberListResponse;
import com.example.demo.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) { this.service = service; }

    @GetMapping
    public List<MemberListResponse> getMembers() { return service.getMembers(); }

    @GetMapping("/{id}")
    public MemberDetailsResponse getMember(@PathVariable Integer id) { return service.getMemberById(id); }
}
