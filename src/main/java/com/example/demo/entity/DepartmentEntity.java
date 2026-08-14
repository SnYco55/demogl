package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({
        "id",
        "facultyId",
        "createdAt"
})

@Entity
@Table(name = "departments")
public class DepartmentEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    @JsonIgnore
    private FacultyEntity faculty;

    @OneToMany(mappedBy = "department")
    private List<ServiceEntity> services = new ArrayList<>();



    public String getId() { return id; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public FacultyEntity getFaculty() { return faculty; }
    public void setFaculty(FacultyEntity faculty) { this.faculty = faculty; }

    public String getFacultyId() { return faculty.getId(); }

}
