package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "members")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "start_date", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE"
    )
    private LocalDateTime start;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime end;

    @OneToMany(mappedBy = "director")
    private List<ServiceEntity> directedServices;

    @ManyToMany
    @JoinTable(
            name = "members_to_services",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<ServiceEntity> services = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "members_to_roles",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();



    public Integer getId() { return id; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }

    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }

    public Set<ServiceEntity> getServices() { return services; }

    public Set<RoleEntity> getRoles() { return roles; }


}