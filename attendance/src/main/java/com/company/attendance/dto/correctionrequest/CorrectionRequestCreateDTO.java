
package com.company.attendance.dto.correctionrequest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CorrectionRequestCreateDTO {
    @NotNull(message = "Attendance ID is required")
    private UUID attendanceId;

    private LocalTime checkIn;

    private LocalTime checkOut;

    @NotNull(message = "Reason is required")
    private String reason;
}
