package com.example.demo.service;

import com.example.demo.dto.member.MemberCreateRequest;
import com.example.demo.dto.member.MemberDetailsResponse;
import com.example.demo.dto.member.MemberListResponse;
import com.example.demo.dto.member.MemberPatchRequest;
import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.ServiceEntity;
import com.example.demo.mapper.Mapper;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.ServiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service
public class MemberService {


    private final MemberRepository repository;
    private final Mapper mapper;
    private final ServiceRepository serviceRepository;
    private final RoleRepository roleRepository;

    public MemberService(MemberRepository repository, Mapper mapper, ServiceRepository serviceRepository, RoleRepository roleRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceRepository = serviceRepository;
        this.roleRepository = roleRepository;
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

    public MemberDetailsResponse createMember(MemberCreateRequest request) {
        validateMemberRequest(request.firstname(), request.lastname());

        MemberEntity member = new MemberEntity();
        member.setFirstname(normalizeText(request.firstname()));
        member.setLastname(normalizeText(request.lastname()));

        LocalDateTime start = request.start() != null
                ? request.start()
                : LocalDateTime.now();

        member.setStart(start);

        member.setEnd(request.end());
        member.getServices().addAll(resolveServices(request.serviceIds()));
        member.getRoles().addAll(resolveRoles(request.roleIds()));

        repository.save(member);
        return mapper.toDetailsResponse(member);
    }

    public MemberDetailsResponse updateMember(Integer id, MemberPatchRequest request) {
        MemberEntity member = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Member not found"
                ));

        if (request.firstname() != null) {
            if (request.firstname().isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Member firstname cannot be blank"
                );
            }
            member.setFirstname(normalizeText(request.firstname()));
        }

        if (request.lastname() != null) {
            if (request.lastname().isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Member lastname cannot be blank"
                );
            }
            member.setLastname(normalizeText(request.lastname()));
        }

        if (request.start() != null) {
            member.setStart(request.start());
        }

        if (request.end() != null) {
            member.setEnd(request.end());
        }

        if (request.serviceIds() != null) {
            member.getServices().clear();
            member.getServices().addAll(resolveServices(request.serviceIds()));
        }

        if (request.roleIds() != null) {
            member.getRoles().clear();
            member.getRoles().addAll(resolveRoles(request.roleIds()));
        }

        repository.save(member);

        return mapper.toDetailsResponse(member);
    }

    public void deleteMember(Integer id) {
        MemberEntity member = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Member not found"
                ));

        long directedServicesCount = serviceRepository.countByDirector_Id(member.getId());
        if (directedServicesCount > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Member cannot be deleted while it is director of services"
            );
        }

        repository.delete(member);
    }

    private void validateMemberRequest(String firstname, String lastname) {
        if (firstname == null || firstname.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member firstname is required");
        }
        if (lastname == null || lastname.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member lastname is required");
        }
    }

    private String normalizeText(String value) {
        return value.trim();
    }

    private Set<ServiceEntity> resolveServices(List<String> serviceIds) {
        if (serviceIds == null || serviceIds.isEmpty()) {
            return new HashSet<>();
        }

        return new HashSet<>(serviceRepository.findAllById(serviceIds));
    }

    private Set<RoleEntity> resolveRoles(List<Integer> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new HashSet<>();
        }

        return new HashSet<>(roleRepository.findAllById(roleIds));
    }
}
