
package com.company.attendance.mapper;

import com.company.attendance.dto.workshift.WorkShiftCreateDTO;
import com.company.attendance.dto.workshift.WorkShiftResponseDTO;
import com.company.attendance.dto.workshift.WorkShiftUpdateDTO;
import com.company.attendance.model.entity.WorkShift;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkShiftMapper {
    WorkShiftResponseDTO toWorkShiftResponseDTO(WorkShift workShift);
    WorkShift toWorkShift(WorkShiftCreateDTO workShiftCreateDTO);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateWorkShiftFromDTO(WorkShiftUpdateDTO workShiftUpdateDTO, @MappingTarget WorkShift workShift);
}
