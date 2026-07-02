
package com.company.attendance.dto.correctionrequest;

import com.company.attendance.model.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CorrectionRequestResponseDTO {
    private UUID id;
    private UUID userId;
    private String userName;
    private UUID attendanceId;
    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private String reason;
    private RequestStatus status;
    private String rejectionReason;
    private UUID approvedById;
    private String approvedByName;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
