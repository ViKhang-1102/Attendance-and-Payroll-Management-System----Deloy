
package com.company.attendance.repository;

import com.company.attendance.model.entity.CorrectionRequest;
import com.company.attendance.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CorrectionRequestRepository extends JpaRepository<CorrectionRequest, UUID> {
    List<CorrectionRequest> findByUser(User user);
}
