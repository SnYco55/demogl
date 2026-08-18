package com.example.demo.repository;

import com.example.demo.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, String> {

	long countByFaculty_Id(String facultyId);
}