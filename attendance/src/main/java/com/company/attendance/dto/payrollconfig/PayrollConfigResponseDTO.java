
package com.company.attendance.dto.payrollconfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayrollConfigResponseDTO {
    private UUID id;
    private BigDecimal overtimeMultiplier;
    private BigDecimal insuranceRate;
    private BigDecimal taxRate;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
