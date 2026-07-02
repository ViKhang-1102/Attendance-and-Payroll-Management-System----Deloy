
package com.company.attendance.dto.workshift;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkShiftResponseDTO {
    private UUID id;
    private String name;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Integer graceMinutes;
    private BigDecimal overtimeHourlyRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
