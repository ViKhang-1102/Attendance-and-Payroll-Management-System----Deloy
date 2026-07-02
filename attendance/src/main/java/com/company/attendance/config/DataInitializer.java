package com.company.attendance.config;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.company.attendance.model.entity.*;
import com.company.attendance.model.enums.*;
import com.company.attendance.repository.*;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final WorkShiftRepository workShiftRepository;
    private final PayrollConfigRepository payrollConfigRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final CorrectionRequestRepository correctionRequestRepository;
    private final PayrollRepository payrollRepository;

    @Override
    public void run(String... args) {
        try {
            // 1. Tạo Chức vụ (Positions)
            Position devPosition = null, managerPosition = null, designerPosition = null, testerPosition = null, accountantPosition = null;
            if (positionRepository.count() == 0) {
                devPosition = positionRepository.save(Position.builder().name("Software Developer").description("Develops software").build());
                managerPosition = positionRepository.save(Position.builder().name("Project Manager").description("Manages teams").build());
                designerPosition = positionRepository.save(Position.builder().name("UI/UX Designer").description("Creates designs").build());
                testerPosition = positionRepository.save(Position.builder().name("Quality Assurance").description("Tests software").build());
                accountantPosition = positionRepository.save(Position.builder().name("Accountant").description("Manages financial records").build());
                System.out.println("Default positions created");
            } else {
                List<Position> positions = positionRepository.findAll();
                devPosition = positions.get(0);
                managerPosition = positions.size() > 1 ? positions.get(1) : devPosition;
                designerPosition = positions.size() > 2 ? positions.get(2) : devPosition;
                testerPosition = positions.size() > 3 ? positions.get(3) : devPosition;
                accountantPosition = positions.size() > 4 ? positions.get(4) : devPosition;
            }

            // 2. Tạo Ca làm việc (Work Shifts)
            WorkShift standardShift = null;
            if (workShiftRepository.count() == 0) {
                standardShift = workShiftRepository.save(WorkShift.builder()
                        .name("Ca hành chính (08:00 - 17:00)")
                        .checkInTime(java.time.LocalTime.of(8,0))
                        .checkOutTime(java.time.LocalTime.of(17,0))
                        .graceMinutes(15)
                        .overtimeHourlyRate(BigDecimal.valueOf(75000))
                        .build());
                workShiftRepository.save(WorkShift.builder()
                        .name("Ca đêm (21:00 - 06:00)")
                        .checkInTime(java.time.LocalTime.of(21,0))
                        .checkOutTime(java.time.LocalTime.of(6,0))
                        .graceMinutes(10)
                        .overtimeHourlyRate(BigDecimal.valueOf(100000))
                        .build());
                System.out.println("Default work shifts created");
            } else {
                standardShift = workShiftRepository.findAll().get(0);
            }

            // 3. Tạo Cấu hình Lương (Payroll Config)
            if (payrollConfigRepository.count() == 0) {
                payrollConfigRepository.save(PayrollConfig.builder()
                        .overtimeMultiplier(new BigDecimal("1.5"))
                        .insuranceRate(new BigDecimal("10.5"))
                        .taxRate(new BigDecimal("20.0"))
                        .isActive(true)
                        .build());
            }

            // 4. Tạo Phòng ban (Departments)
            Department itDepartment = null, financeDepartment = null, marketingDepartment = null;
            if (departmentRepository.count() == 0) {
                itDepartment = departmentRepository.save(Department.builder().name("IT Department").description("Information Technology").build());
                departmentRepository.save(Department.builder().name("HR Department").description("Human Resources").build());
                financeDepartment = departmentRepository.save(Department.builder().name("Finance Department").description("Manages finances").build());
                marketingDepartment = departmentRepository.save(Department.builder().name("Marketing Department").description("Promotes products").build());
                System.out.println("Default departments created");
            } else {
                List<Department> departments = departmentRepository.findAll();
                itDepartment = departments.get(0);
                financeDepartment = departments.size() > 2 ? departments.get(2) : itDepartment;
                marketingDepartment = departments.size() > 3 ? departments.get(3) : itDepartment;
            }

            // 5. Tạo các User mẫu (Kiểm tra chặn trùng lặp cả Email lẫn StaffId cực kỳ nghiêm ngặt)
            createIfNotExist("ADMIN001", "admin@company.com", "admin123", "System Administrator", Role.ADMIN, new BigDecimal("20000000"), managerPosition, itDepartment, standardShift);
            createIfNotExist("EMP001", "employee1@company.com", "employee123", "Nguyễn Văn A", Role.EMPLOYEE, new BigDecimal("10000000"), devPosition, itDepartment, standardShift);
            createIfNotExist("EMP002", "employee2@company.com", "employee123", "Trần Thị B", Role.EMPLOYEE, new BigDecimal("12000000"), designerPosition, itDepartment, standardShift);
            createIfNotExist("EMP003", "employee3@company.com", "employee123", "Lê Văn C", Role.EMPLOYEE, new BigDecimal("11000000"), testerPosition, itDepartment, standardShift);
            createIfNotExist("EMP004", "employee4@company.com", "employee123", "Phạm Thị D", Role.EMPLOYEE, new BigDecimal("11500000"), accountantPosition, financeDepartment, standardShift);
            createIfNotExist("EMP005", "employee5@company.com", "employee123", "Hoàng Văn E", Role.EMPLOYEE, new BigDecimal("13000000"), managerPosition, marketingDepartment, standardShift);
            createIfNotExist("EMP006", "employee6@company.com", "employee123", "Đỗ Thị F", Role.EMPLOYEE, new BigDecimal("10500000"), designerPosition, marketingDepartment, standardShift);

            // 6. Tạo dữ liệu chấm công mẫu an toàn
            if (attendanceRepository.count() == 0) {
                // Bạn có thể giữ hoặc tạm thời clear phần seed attendance này để giảm tải bộ nhớ DB Neon 
                System.out.println("No attendance data found. Ready for active tracking.");
            }

        } catch (Exception e) {
            System.err.println("👉 CẢNH BÁO: Quá trình nạp dữ liệu mẫu gặp lỗi xung đột DB, nhưng hệ thống đã bỏ qua để đảm bảo Server không sập: " + e.getMessage());
        }
    }

    private void createIfNotExist(String staffId, String email, String rawPassword, String fullName, Role role, BigDecimal salary, Position pos, Department dept, WorkShift shift) {
        // Chỉ lưu khi chưa tồn tại cả Email VÀ StaffID trong Database
        if (!userRepository.existsByEmail(email) && userRepository.findAll().stream().noneMatch(u -> staffId.equals(u.getStaffId()))) {
            User user = User.builder()
                    .staffId(staffId)
                    .email(email)
                    .password(passwordEncoder.encode(rawPassword)) // Đảm bảo luôn được mã hóa BCrypt đúng chuẩn
                    .fullName(fullName)
                    .role(role)
                    .hourlyRate(BigDecimal.valueOf(50000))
                    .baseSalary(salary)
                    .position(pos)
                    .department(dept)
                    .workShift(shift)
                    .leaveBalance(12)
                    .isActive(true)
                    .build();
            userRepository.save(user);
            System.out.println("Successfully seeded user: " + email);
        }
    }
}