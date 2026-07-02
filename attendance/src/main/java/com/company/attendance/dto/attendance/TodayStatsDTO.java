
package com.company.attendance.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodayStatsDTO {
    private long totalEmployees;
    private long presentCount;
    private long absentCount;
    private long leaveCount;
}
