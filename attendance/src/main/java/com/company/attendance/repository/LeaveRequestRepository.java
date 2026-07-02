
package com.company.attendance.repository;

import com.company.attendance.model.entity.LeaveRequest;
import com.company.attendance.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, UUID> {
    List<LeaveRequest> findByUser(User user);
    List<LeaveRequest> findByUserId(UUID userId);
}

