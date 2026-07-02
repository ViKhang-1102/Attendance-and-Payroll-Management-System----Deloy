
package com.company.attendance.mapper;

import com.company.attendance.dto.payroll.PayrollCreateDTO;
import com.company.attendance.dto.payroll.PayrollResponseDTO;
import com.company.attendance.dto.payroll.PayrollUpdateDTO;
import com.company.attendance.model.entity.Payroll;
import com.company.attendance.model.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PayrollMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.fullName")
    @Mapping(target = "month", source = "monthNum")
    @Mapping(target = "year", source = "yearNum")
    PayrollResponseDTO toPayrollResponseDTO(Payroll payroll);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "monthNum", source = "payrollCreateDTO.month")
    @Mapping(target = "yearNum", source = "payrollCreateDTO.year")
    @Mapping(target = "totalHours", ignore = true)
    @Mapping(target = "overtimeHours", ignore = true)
    @Mapping(target = "overtimePay", ignore = true)
    @Mapping(target = "netSalary", ignore = true)
    @Mapping(target = "workingDays", ignore = true)
    @Mapping(target = "absentDays", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isPaid", ignore = true)
    @Mapping(target = "paidAt", ignore = true)
    Payroll toPayroll(PayrollCreateDTO payrollCreateDTO, User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "totalHours", ignore = true)
    @Mapping(target = "overtimeHours", ignore = true)
    @Mapping(target = "overtimePay", ignore = true)
    @Mapping(target = "netSalary", ignore = true)
    @Mapping(target = "workingDays", ignore = true)
    @Mapping(target = "absentDays", ignore = true)
    void updatePayrollFromDTO(PayrollUpdateDTO payrollUpdateDTO, @MappingTarget Payroll payroll);
}
