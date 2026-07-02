
package com.company.attendance.dto.user;

import com.company.attendance.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private UUID id;
    private String staffId;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private Role role;
    private BigDecimal hourlyRate;
    private BigDecimal baseSalary;
    private UUID positionId;
    private String positionName;
    private UUID departmentId;
    private String departmentName;
    private UUID workShiftId;
    private String workShiftName;
    private Integer leaveBalance;
    private Boolean isActive;
    private String avatarUrl;
    private String cccdUrl;
    private String contractUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
