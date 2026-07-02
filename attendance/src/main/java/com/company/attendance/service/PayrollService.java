
package com.company.attendance.service;

import com.company.attendance.dto.payroll.PayrollCreateDTO;
import com.company.attendance.dto.payroll.PayrollResponseDTO;
import com.company.attendance.dto.payroll.PayrollUpdateDTO;
import com.company.attendance.mapper.PayrollMapper;
import com.company.attendance.model.entity.Attendance;
import com.company.attendance.model.entity.Payroll;
import com.company.attendance.model.entity.PayrollConfig;
import com.company.attendance.model.entity.User;
import com.company.attendance.model.enums.PayrollStatus;
import com.company.attendance.repository.AttendanceRepository;
import com.company.attendance.repository.PayrollConfigRepository;
import com.company.attendance.repository.PayrollRepository;
import com.company.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final PayrollConfigRepository payrollConfigRepository;
    private final PayrollMapper payrollMapper;

    private static final BigDecimal DEFAULT_HOURLY_RATE = BigDecimal.valueOf(100000);
    private static final BigDecimal PERCENTAGE_DIVISOR = BigDecimal.valueOf(100);

    // ==================== CRUD OPERATIONS ====================
    public List<PayrollResponseDTO> getAllPayrolls() {
        return payrollRepository.findAll()
                .stream()
                .map(payrollMapper::toPayrollResponseDTO)
                .toList();
    }

    public PayrollResponseDTO getPayrollById(UUID id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payroll not found"));
        return payrollMapper.toPayrollResponseDTO(payroll);
    }

    public List<PayrollResponseDTO> getPayrollsByUserId(UUID userId) {
        return payrollRepository.findByUserId(userId)
                .stream()
                .map(payrollMapper::toPayrollResponseDTO)
                .toList();
    }

    public List<PayrollResponseDTO> getPayrollsByMonthAndYear(Integer month, Integer year) {
        return payrollRepository.findByMonthNumAndYearNum(month, year)
                .stream()
                .map(payrollMapper::toPayrollResponseDTO)
                .toList();
    }

    public PayrollResponseDTO updatePayroll(UUID id, PayrollUpdateDTO payrollUpdateDTO) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payroll not found"));
        payrollMapper.updatePayrollFromDTO(payrollUpdateDTO, payroll);

        if (payrollUpdateDTO.getIsPaid() != null && payrollUpdateDTO.getIsPaid() && payroll.getPaidAt() == null) {
            payroll.setPaidAt(LocalDateTime.now());
            payroll.setStatus(PayrollStatus.PAID);
        }

        if (payrollUpdateDTO.getBaseSalary() != null ||
                payrollUpdateDTO.getAllowances() != null ||
                payrollUpdateDTO.getDeductions() != null) {
            calculateNetSalary(payroll);
        }

        payroll = payrollRepository.save(payroll);
        return payrollMapper.toPayrollResponseDTO(payroll);
    }

    public void deletePayroll(UUID id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payroll not found"));
        payrollRepository.delete(payroll);
    }

    // ==================== GENERATE PAYROLL ====================
    public PayrollResponseDTO generatePayrollForUser(PayrollCreateDTO payrollCreateDTO) {
        User user = userRepository.findById(payrollCreateDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot generate payroll for inactive user");
        }

        if (payrollRepository.findByUserIdAndMonthNumAndYearNum(
                user.getId(), payrollCreateDTO.getMonth(), payrollCreateDTO.getYear()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Payroll already exists for this user in this month/year");
        }

        Payroll payroll = payrollMapper.toPayroll(payrollCreateDTO, user);
        populatePayrollFromAttendance(payroll, user, payrollCreateDTO.getMonth(), payrollCreateDTO.getYear());
        if (payrollCreateDTO.getAllowances() != null) {
            payroll.setAllowances(payrollCreateDTO.getAllowances());
        } else {
            payroll.setAllowances(BigDecimal.ZERO);
        }
        if (payrollCreateDTO.getDeductions() != null) {
            payroll.setDeductions(payrollCreateDTO.getDeductions());
        } else {
            payroll.setDeductions(BigDecimal.ZERO);
        }
        calculateNetSalary(payroll);

        payroll.setStatus(PayrollStatus.PENDING);
        payroll.setIsPaid(false);

        payroll = payrollRepository.save(payroll);
        return payrollMapper.toPayrollResponseDTO(payroll);
    }

    public List<PayrollResponseDTO> generatePayrollForAllEmployees(Integer month, Integer year) {
        List<User> employees = userRepository.findAll()
                .stream()
                .filter(user -> user.getRole().name().equals("EMPLOYEE"))
                .filter(User::getIsActive)
                .toList();

        List<PayrollResponseDTO> generatedPayrolls = new java.util.ArrayList<>();
        for (User employee : employees) {
            if (payrollRepository.findByUserIdAndMonthNumAndYearNum(employee.getId(), month, year).isEmpty()) {
                PayrollCreateDTO createDTO = PayrollCreateDTO.builder()
                        .userId(employee.getId())
                        .month(month)
                        .year(year)
                        .allowances(BigDecimal.ZERO)
                        .deductions(BigDecimal.ZERO)
                        .build();
                PayrollResponseDTO payroll = generatePayrollForUser(createDTO);
                generatedPayrolls.add(payroll);
            }
        }
        return generatedPayrolls;
    }

    // ==================== HELPER METHODS ====================
    private void populatePayrollFromAttendance(Payroll payroll, User user, Integer month, Integer year) {
        YearMonth yearMonth = YearMonth.of(year, month);
        java.time.LocalDate startDate = yearMonth.atDay(1);
        java.time.LocalDate endDate = yearMonth.atEndOfMonth();

        List<Attendance> attendances = attendanceRepository.findByUserIdAndDateRange(user.getId(), startDate, endDate);

        BigDecimal totalHours = BigDecimal.ZERO;
        BigDecimal overtimeHours = BigDecimal.ZERO;
        int workingDays = 0;
        int absentDays = 0;

        for (Attendance attendance : attendances) {
            if (attendance.getStatus() == com.company.attendance.model.enums.AttendanceStatus.PRESENT ||
                    attendance.getStatus() == com.company.attendance.model.enums.AttendanceStatus.LATE ||
                    attendance.getStatus() == com.company.attendance.model.enums.AttendanceStatus.HALF_DAY) {
                workingDays++;
                if (attendance.getHoursWorked() != null) {
                    totalHours = totalHours.add(attendance.getHoursWorked());
                }
                if (attendance.getOvertimeHours() != null) {
                    overtimeHours = overtimeHours.add(attendance.getOvertimeHours());
                }
            } else if (attendance.getStatus() == com.company.attendance.model.enums.AttendanceStatus.ABSENT) {
                absentDays++;
            }
        }

        payroll.setTotalHours(totalHours);
        payroll.setOvertimeHours(overtimeHours);
        payroll.setWorkingDays(workingDays);
        payroll.setAbsentDays(absentDays);

        if (user.getBaseSalary() != null) {
            payroll.setBaseSalary(user.getBaseSalary());
        } else {
            payroll.setBaseSalary(BigDecimal.ZERO);
        }
    }

    private void calculateNetSalary(Payroll payroll) {
        BigDecimal hourlyRate = payroll.getUser() != null && payroll.getUser().getHourlyRate() != null
                ? payroll.getUser().getHourlyRate()
                : DEFAULT_HOURLY_RATE;
                
        BigDecimal otRate = hourlyRate;
        if (payroll.getUser() != null && payroll.getUser().getWorkShift() != null 
                && payroll.getUser().getWorkShift().getOvertimeHourlyRate() != null) {
            otRate = payroll.getUser().getWorkShift().getOvertimeHourlyRate();
        }

        PayrollConfig config = payrollConfigRepository.findByIsActiveTrue()
                .orElse(PayrollConfig.builder()
                        .overtimeMultiplier(BigDecimal.valueOf(1.5))
                        .insuranceRate(BigDecimal.valueOf(10.5))
                        .taxRate(BigDecimal.valueOf(20.0))
                        .build());

        BigDecimal overtimePay = payroll.getOvertimeHours() != null
                ? payroll.getOvertimeHours().multiply(otRate).multiply(config.getOvertimeMultiplier())
                        .setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        payroll.setOvertimePay(overtimePay);

        BigDecimal allowances = payroll.getAllowances() != null ? payroll.getAllowances() : BigDecimal.ZERO;
        BigDecimal deductions = payroll.getDeductions() != null ? payroll.getDeductions() : BigDecimal.ZERO;
        BigDecimal baseSalary = payroll.getBaseSalary() != null ? payroll.getBaseSalary() : BigDecimal.ZERO;

        BigDecimal grossSalary = baseSalary
                .add(overtimePay)
                .add(allowances);

        BigDecimal insurance = grossSalary.multiply(config.getInsuranceRate()).divide(PERCENTAGE_DIVISOR, 2, RoundingMode.HALF_UP);
        BigDecimal tax = grossSalary.multiply(config.getTaxRate()).divide(PERCENTAGE_DIVISOR, 2, RoundingMode.HALF_UP);

        payroll.setInsurance(insurance);
        payroll.setTax(tax);

        BigDecimal netSalary = grossSalary
                .subtract(insurance)
                .subtract(tax)
                .subtract(deductions)
                .setScale(2, RoundingMode.HALF_UP);

        payroll.setNetSalary(netSalary);
    }
}
