
package com.company.attendance.dto.leaverequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestApproveDTO {
    private String rejectionReason; // Only needed if rejecting
}

