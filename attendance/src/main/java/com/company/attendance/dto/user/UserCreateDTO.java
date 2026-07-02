
package com.company.attendance.dto.user;

import com.company.attendance.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserCreateDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private String staffId;

    private String phone;

    private String address;

    private LocalDate dateOfBirth;

    @NotNull(message = "Role is required")
    private Role role;

    private BigDecimal hourlyRate;

    private BigDecimal baseSalary;

    private Integer leaveBalance;

    private Boolean isActive;

    private String avatarUrl;
    private String cccdUrl;
    private String contractUrl;

    private UUID positionId;
    private UUID departmentId;
    private UUID workShiftId;
}

