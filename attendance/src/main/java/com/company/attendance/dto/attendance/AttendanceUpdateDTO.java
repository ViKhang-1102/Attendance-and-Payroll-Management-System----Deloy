
package com.company.attendance.dto.attendance;

import com.company.attendance.model.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceUpdateDTO {
    private LocalTime checkIn;
    private LocalTime checkOut;
    private AttendanceStatus status;
    private String notes;
}

