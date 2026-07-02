package com.company.attendance.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateDTO {
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
}
