
package com.company.attendance.dto.attendance;

import com.company.attendance.dto.user.UserResponseDTO;
import com.company.attendance.model.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceResponseDTO {
    private UUID id;
    private UUID userId;
    private String userName;
    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private AttendanceStatus status;
    private BigDecimal hoursWorked;
    private BigDecimal overtimeHours;
    private String notes;
}

