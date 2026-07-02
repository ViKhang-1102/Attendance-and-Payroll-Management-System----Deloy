
package com.company.attendance.dto.attendance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.company.attendance.model.enums.AttendanceStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceCreateDTO {
    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private LocalTime checkIn;

    private LocalTime checkOut;

    @NotNull(message = "Status is required")
    private AttendanceStatus status;

    private String notes;
}

