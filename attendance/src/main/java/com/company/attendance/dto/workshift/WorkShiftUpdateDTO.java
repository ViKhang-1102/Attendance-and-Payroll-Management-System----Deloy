
package com.company.attendance.dto.workshift;

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
public class WorkShiftUpdateDTO {
    private String name;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Integer graceMinutes;
    private BigDecimal overtimeHourlyRate;
}
