
package com.company.attendance.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.attendance.model.entity.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {
    boolean existsByName(String name);
}
