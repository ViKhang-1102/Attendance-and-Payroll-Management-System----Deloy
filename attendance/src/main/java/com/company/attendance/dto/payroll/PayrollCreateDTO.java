
package com.company.attendance.dto.payroll;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayrollCreateDTO {
    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Month is required")
    private Integer month;

    @NotNull(message = "Year is required")
    private Integer year;

    private BigDecimal allowances;
    private BigDecimal deductions;
    private String notes;
}
