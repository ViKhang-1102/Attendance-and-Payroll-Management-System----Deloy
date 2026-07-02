
package com.company.attendance.mapper;

import com.company.attendance.dto.payrollconfig.PayrollConfigCreateDTO;
import com.company.attendance.dto.payrollconfig.PayrollConfigResponseDTO;
import com.company.attendance.dto.payrollconfig.PayrollConfigUpdateDTO;
import com.company.attendance.model.entity.PayrollConfig;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PayrollConfigMapper {
    PayrollConfigResponseDTO toResponseDTO(PayrollConfig payrollConfig);

    @Mapping(target = "isActive", ignore = true)
    PayrollConfig toEntity(PayrollConfigCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(PayrollConfigUpdateDTO dto, @MappingTarget PayrollConfig payrollConfig);
}
