package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class DepartmentEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private FacultyEntity faculty;

    @OneToMany(mappedBy = "department")
    private List<ServiceEntity> services = new ArrayList<>();



    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public FacultyEntity getFaculty() { return faculty; }
    public void setFaculty(FacultyEntity faculty) { this.faculty = faculty; }

    public String getFacultyId() { return faculty.getId(); }

    public List<ServiceEntity> getServices() { return services; }

}
