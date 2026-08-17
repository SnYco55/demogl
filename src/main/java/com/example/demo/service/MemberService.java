package com.example.demo.service;

import com.example.demo.dto.member.MemberDetailsResponse;
import com.example.demo.dto.member.MemberListResponse;
import com.example.demo.entity.MemberEntity;
import com.example.demo.mapper.Mapper;
import com.example.demo.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository repository;
    private final Mapper mapper;

    public MemberService(MemberRepository repository,  Mapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<MemberListResponse> getMembers() {
        return repository.findAll().stream()
                .map(member -> new MemberListResponse(
                        member.getId(),
                        member.getFirstname(),
                        member.getLastname(),
                        member.getStart(),
                        member.getEnd(),
                        member.getCreatedAt()
                ))
                .toList();
    }

    public MemberDetailsResponse getMemberById(Integer id) {
        MemberEntity member = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Member not found"
                ));

        return mapper.toDetailsResponse(member);
    }
}
