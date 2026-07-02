
package com.company.attendance.mapper;

import com.company.attendance.dto.user.UserCreateDTO;
import com.company.attendance.dto.user.UserResponseDTO;
import com.company.attendance.dto.user.UserUpdateDTO;
import com.company.attendance.model.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "position.id", target = "positionId")
    @Mapping(source = "position.name", target = "positionName")
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "workShift.id", target = "workShiftId")
    @Mapping(source = "workShift.name", target = "workShiftName")
    UserResponseDTO toUserResponseDTO(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "workShift", ignore = true)
    User toUser(UserCreateDTO userCreateDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "workShift", ignore = true)
    void updateUserFromDTO(UserUpdateDTO userUpdateDTO, @MappingTarget User user);
}
