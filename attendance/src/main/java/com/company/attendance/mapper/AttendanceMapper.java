
package com.company.attendance.mapper;

import com.company.attendance.dto.attendance.AttendanceCreateDTO;
import com.company.attendance.dto.attendance.AttendanceResponseDTO;
import com.company.attendance.dto.attendance.AttendanceUpdateDTO;
import com.company.attendance.model.entity.Attendance;
import com.company.attendance.model.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttendanceMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.fullName")
    AttendanceResponseDTO toAttendanceResponseDTO(Attendance attendance);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "hoursWorked", ignore = true)
    @Mapping(target = "overtimeHours", ignore = true)
    Attendance toAttendance(AttendanceCreateDTO attendanceCreateDTO, User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "hoursWorked", ignore = true)
    @Mapping(target = "overtimeHours", ignore = true)
    void updateAttendanceFromDTO(AttendanceUpdateDTO attendanceUpdateDTO, @MappingTarget Attendance attendance);
}

