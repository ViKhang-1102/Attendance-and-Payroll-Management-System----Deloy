
package com.company.attendance.dto.workshift;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkShiftCreateDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Check-in time is required")
    private LocalTime checkInTime;

    @NotNull(message = "Check-out time is required")
    private LocalTime checkOutTime;

    @NotNull(message = "Grace minutes is required")
    private Integer graceMinutes;

    @NotNull(message = "Overtime hourly rate is required")
    private BigDecimal overtimeHourlyRate;
}
