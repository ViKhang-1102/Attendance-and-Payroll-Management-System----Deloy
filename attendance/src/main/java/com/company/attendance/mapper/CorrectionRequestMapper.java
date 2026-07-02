
package com.company.attendance.mapper;

import com.company.attendance.dto.correctionrequest.CorrectionRequestCreateDTO;
import com.company.attendance.dto.correctionrequest.CorrectionRequestResponseDTO;
import com.company.attendance.model.entity.CorrectionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CorrectionRequestMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userName")
    @Mapping(source = "attendance.id", target = "attendanceId")
    @Mapping(source = "approvedBy.id", target = "approvedById")
    @Mapping(source = "approvedBy.fullName", target = "approvedByName")
    CorrectionRequestResponseDTO toResponseDTO(CorrectionRequest correctionRequest);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "attendance", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "approvedAt", ignore = true)
    CorrectionRequest toEntity(CorrectionRequestCreateDTO dto);
}
