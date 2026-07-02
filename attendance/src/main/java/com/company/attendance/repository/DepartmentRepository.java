
package com.company.attendance.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.attendance.model.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    boolean existsByName(String name);
}
