
package com.company.attendance.dto.payroll;

import com.company.attendance.model.enums.PayrollStatus;
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
public class PayrollResponseDTO {
    private UUID id;
    private UUID userId;
    private String userName;
    private Integer month;
    private Integer year;
    private BigDecimal baseSalary;
    private BigDecimal totalHours;
    private BigDecimal overtimeHours;
    private BigDecimal overtimePay;
    private BigDecimal allowances;
    private BigDecimal deductions;
    private BigDecimal netSalary;
    private BigDecimal insurance;
    private BigDecimal tax;
    private Integer workingDays;
    private Integer absentDays;
    private PayrollStatus status;
    private Boolean isPaid;
    private LocalDateTime paidAt;
    private String notes;
}
