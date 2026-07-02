
package com.company.attendance.repository;

import com.company.attendance.model.entity.WorkShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkShiftRepository extends JpaRepository<WorkShift, UUID> {
}
