
package com.company.attendance.dto.payrollconfig;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayrollConfigCreateDTO {
    @NotNull(message = "Overtime multiplier is required")
    private BigDecimal overtimeMultiplier;

    @NotNull(message = "Insurance rate is required")
    private BigDecimal insuranceRate;

    @NotNull(message = "Tax rate is required")
    private BigDecimal taxRate;
}
