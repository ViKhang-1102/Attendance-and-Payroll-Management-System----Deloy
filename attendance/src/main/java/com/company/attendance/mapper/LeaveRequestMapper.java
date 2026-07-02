
package com.company.attendance.mapper;

import com.company.attendance.dto.leaverequest.LeaveRequestCreateDTO;
import com.company.attendance.dto.leaverequest.LeaveRequestResponseDTO;
import com.company.attendance.dto.leaverequest.LeaveRequestUpdateDTO;
import com.company.attendance.model.entity.LeaveRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LeaveRequestMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userName")
    @Mapping(source = "approvedBy.id", target = "approvedByUserId")
    @Mapping(source = "approvedBy.fullName", target = "approvedByUserName")
    LeaveRequestResponseDTO toResponseDTO(LeaveRequest leaveRequest);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "approvedAt", ignore = true)
    LeaveRequest toEntity(LeaveRequestCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "approvedAt", ignore = true)
    void updateEntityFromDTO(LeaveRequestUpdateDTO dto, @MappingTarget LeaveRequest leaveRequest);
}

