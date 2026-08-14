package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@JsonPropertyOrder({
        "id",
        "name",
        "departmentId",
        "directorId",
        "createdAt"
})

@Entity
@Table(name = "services")
public class ServiceEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    @JsonIgnore
    private DepartmentEntity department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id")
    @JsonIgnore
    private MemberEntity director;

    @ManyToMany(mappedBy = "services")
    private Set<MemberEntity> members =  new HashSet<>();



    public String getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public DepartmentEntity getDepartment() { return department; }
    public void setDepartment(DepartmentEntity department) { this.department = department; }

    public String getDepartmentId() { return department.getId(); }

    public Integer getDirectorId() { return director.getId(); }

}
