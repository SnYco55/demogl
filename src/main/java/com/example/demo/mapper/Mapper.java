package com.example.demo.mapper;

import com.example.demo.dto.department.DepartmentResponse;
import com.example.demo.dto.faculty.FacultyResponse;
import com.example.demo.dto.member.MemberDetailsResponse;
import com.example.demo.dto.member.MemberListResponse;
import com.example.demo.dto.role.RoleResponse;
import com.example.demo.dto.service.ServiceDetailsResponse;
import com.example.demo.entity.MemberEntity;
import com.example.demo.entity.ServiceEntity;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public MemberDetailsResponse toDetailsResponse(MemberEntity member) {

        return new MemberDetailsResponse(
                member.getId(),
                member.getFirstname(),
                member.getLastname(),
                member.getStart(),
                member.getEnd(),

                member.getServices()
                        .stream()
                        .map(this::toServiceDetailsResponse)
                        .toList(),

                member.getRoles()
                        .stream()
                        .map(role -> new RoleResponse(
                                role.getId(),
                                role.getName(),
                                role.getCreatedAt()
                        ))
                        .toList(),

                member.getCreatedAt()
        );
    }

    public ServiceDetailsResponse toServiceDetailsResponse(ServiceEntity service) {

        return new ServiceDetailsResponse(
                service.getId(),
                service.getName(),

                new DepartmentResponse(
                        service.getDepartment().getId(),
                        service.getDepartment().getFaculty().getId(),
                        service.getDepartment().getCreatedAt()
                ),

                new FacultyResponse(
                        service.getDepartment().getFaculty().getId(),
                        service.getDepartment().getFaculty().getName(),
                        service.getDepartment().getFaculty().getCreatedAt()
                ),

                new MemberListResponse(
                        service.getDirector().getId(),
                        service.getDirector().getFirstname(),
                        service.getDirector().getLastname(),
                        service.getDirector().getStart(),
                        service.getDirector().getEnd(),
                        service.getDirector().getCreatedAt()
                ),

                service.getCreatedAt()
        );
    }
}