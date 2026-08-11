package com.example.demo.service;

import com.example.demo.entity.MemberEntity;
import com.example.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public List<MemberEntity> getMembers() {
        return repository.findAll();
    }
}
