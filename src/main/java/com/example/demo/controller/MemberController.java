package com.example.demo.controller;

import com.example.demo.dto.member.MemberCreateRequest;
import com.example.demo.dto.member.MemberDetailsResponse;
import com.example.demo.dto.member.MemberListResponse;
import com.example.demo.dto.member.MemberPatchRequest;
import com.example.demo.service.MemberService;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberDetailsResponse createMember(@RequestBody MemberCreateRequest request) {
        return service.createMember(request);
    }

    @PatchMapping("/{id}")
    public MemberDetailsResponse updateMember(@PathVariable Integer id, @RequestBody MemberPatchRequest request) {
        return service.updateMember(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@PathVariable Integer id) {
        service.deleteMember(id);
    }
}
