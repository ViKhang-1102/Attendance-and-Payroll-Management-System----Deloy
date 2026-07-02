
package com.company.attendance.dto.payroll;

import com.company.attendance.model.enums.PayrollStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayrollUpdateDTO {
    private BigDecimal baseSalary;
    private BigDecimal allowances;
    private BigDecimal deductions;
    private PayrollStatus status;
    private Boolean isPaid;
    private String notes;
}
