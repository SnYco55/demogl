package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "created_at",insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "roles")
    private Set<MemberEntity> members = new HashSet<>();



    public Integer getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public Set<MemberEntity> getMembers() { return members; }

}
