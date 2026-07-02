
package com.company.attendance.repository;

import com.company.attendance.model.entity.PayrollConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayrollConfigRepository extends JpaRepository<PayrollConfig, UUID> {
    Optional<PayrollConfig> findByIsActiveTrue();
}
