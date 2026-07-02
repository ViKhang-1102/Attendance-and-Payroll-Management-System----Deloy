
package com.company.attendance.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.attendance.model.entity.User;
import com.company.attendance.model.enums.Role;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailOrStaffId(String email, String staffId);
    boolean existsByEmail(String email);
    List<User> findByIsActiveTrueAndRoleNot(Role role);
}
