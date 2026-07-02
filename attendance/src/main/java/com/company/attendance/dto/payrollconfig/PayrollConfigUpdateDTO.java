
package com.company.attendance.dto.payrollconfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayrollConfigUpdateDTO {
    private BigDecimal overtimeMultiplier;
    private BigDecimal insuranceRate;
    private BigDecimal taxRate;
    private Boolean isActive;
}
