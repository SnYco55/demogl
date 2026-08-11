package com.example.demo.controller;

import com.example.demo.entity.MemberEntity;
import com.example.demo.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @GetMapping
    public List<MemberEntity> getMembers() {
        return service.getMembers();
    }
}
