
package com.company.attendance.dto;

import com.company.attendance.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private UUID userId;
    private String staffId;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private Role role;
    private BigDecimal hourlyRate;
    private BigDecimal baseSalary;
    private String positionName;
    private String departmentName;
    private Integer leaveBalance;
    private String avatarUrl;
    private Boolean isActive;
}
