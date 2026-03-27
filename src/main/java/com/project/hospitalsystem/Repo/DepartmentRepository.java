package com.project.hospitalsystem.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.hospitalsystem.Entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
}
