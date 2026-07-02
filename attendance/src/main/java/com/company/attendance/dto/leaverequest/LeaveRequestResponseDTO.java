
package com.company.attendance.dto.leaverequest;

import com.company.attendance.model.enums.LeaveType;
import com.company.attendance.model.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestResponseDTO {
    private UUID id;
    private UUID userId;
    private String userName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveType leaveType;
    private String reason;
    private RequestStatus status;
    private String rejectionReason;
    private UUID approvedByUserId;
    private String approvedByUserName;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

