
package com.company.attendance.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.company.attendance.model.entity.Department;
import com.company.attendance.model.entity.Position;
import com.company.attendance.model.entity.User;
import com.company.attendance.model.entity.WorkShift;
import com.company.attendance.model.enums.Role;
import com.company.attendance.repository.DepartmentRepository;
import com.company.attendance.repository.PositionRepository;
import com.company.attendance.repository.UserRepository;
import com.company.attendance.repository.WorkShiftRepository;
import com.company.attendance.repository.PayrollConfigRepository;
import com.company.attendance.model.entity.PayrollConfig;
import com.company.attendance.repository.AttendanceRepository;
import com.company.attendance.repository.LeaveRequestRepository;
import com.company.attendance.repository.CorrectionRequestRepository;
import com.company.attendance.repository.PayrollRepository;
import com.company.attendance.model.entity.Attendance;
import com.company.attendance.model.entity.LeaveRequest;
import com.company.attendance.model.entity.CorrectionRequest;
import com.company.attendance.model.entity.Payroll;
import com.company.attendance.model.enums.AttendanceStatus;
import com.company.attendance.model.enums.LeaveType;
import com.company.attendance.model.enums.RequestStatus;
import com.company.attendance.model.enums.PayrollStatus;

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
        // Clear old mock transactional data to ensure fresh seeding relative to current system date
        correctionRequestRepository.deleteAll();
        attendanceRepository.deleteAll();
        leaveRequestRepository.deleteAll();
        payrollRepository.deleteAll();

        // Create default positions if not exist
        Position devPosition = null;
        Position managerPosition = null;
        Position designerPosition = null;
        Position testerPosition = null;
        Position accountantPosition = null;
        if (positionRepository.count() == 0) {
            devPosition = Position.builder()
                    .name("Software Developer")
                    .description("Develops and maintains software applications")
                    .build();
            positionRepository.save(devPosition);
            
            managerPosition = Position.builder()
                    .name("Project Manager")
                    .description("Manages projects and teams")
                    .build();
            positionRepository.save(managerPosition);
            
            designerPosition = Position.builder()
                    .name("UI/UX Designer")
                    .description("Creates user interface and experience designs")
                    .build();
            positionRepository.save(designerPosition);
            
            testerPosition = Position.builder()
                    .name("Quality Assurance")
                    .description("Tests software to ensure quality")
                    .build();
            positionRepository.save(testerPosition);
            
            accountantPosition = Position.builder()
                    .name("Accountant")
                    .description("Manages financial records and reports")
                    .build();
            positionRepository.save(accountantPosition);
            
            System.out.println("Default positions created");
        } else {
            List<Position> positions = positionRepository.findAll();
            devPosition = positions.get(0);
            managerPosition = positions.size() > 1 ? positions.get(1) : devPosition;
            designerPosition = positions.size() > 2 ? positions.get(2) : devPosition;
            testerPosition = positions.size() > 3 ? positions.get(3) : devPosition;
            accountantPosition = positions.size() > 4 ? positions.get(4) : devPosition;
        }

        // Create default work shifts if not exist
        WorkShift standardShift = null;
        WorkShift nightShift = null;
        if (workShiftRepository.count() == 0) {
            standardShift = WorkShift.builder()
                    .name("Ca hành chính (08:00 - 17:00)")
                    .checkInTime(java.time.LocalTime.of(8,0))
                    .checkOutTime(java.time.LocalTime.of(17,0))
                    .graceMinutes(15)
                    .overtimeHourlyRate(java.math.BigDecimal.valueOf(75000))
                    .build();
            workShiftRepository.save(standardShift);

            nightShift = WorkShift.builder()
                    .name("Ca đêm (21:00 - 06:00)")
                    .checkInTime(java.time.LocalTime.of(21,0))
                    .checkOutTime(java.time.LocalTime.of(6,0))
                    .graceMinutes(10)
                    .overtimeHourlyRate(java.math.BigDecimal.valueOf(100000))
                    .build();
            workShiftRepository.save(nightShift);

            System.out.println("Default work shifts created");
        } else {
            List<WorkShift> workShifts = workShiftRepository.findAll();
            standardShift = workShifts.get(0);
            nightShift = workShifts.size() > 1 ? workShifts.get(1) : standardShift;
        }

        // Create default payroll config if not exist
        if (payrollConfigRepository.count() == 0) {
            PayrollConfig defaultConfig = PayrollConfig.builder()
                    .overtimeMultiplier(new java.math.BigDecimal("1.5"))
                    .insuranceRate(new java.math.BigDecimal("10.5"))
                    .taxRate(new java.math.BigDecimal("20.0"))
                    .isActive(true)
                    .build();
            payrollConfigRepository.save(defaultConfig);
            System.out.println("Default payroll config created");
        }

        // Create default departments if not exist
        Department itDepartment = null;
        Department hrDepartment = null;
        Department financeDepartment = null;
        Department marketingDepartment = null;
        if (departmentRepository.count() == 0) {
            itDepartment = Department.builder()
                    .name("IT Department")
                    .description("Information Technology department")
                    .build();
            departmentRepository.save(itDepartment);
            
            hrDepartment = Department.builder()
                    .name("HR Department")
                    .description("Human Resources department")
                    .build();
            departmentRepository.save(hrDepartment);
            
            financeDepartment = Department.builder()
                    .name("Finance Department")
                    .description("Manages company finances")
                    .build();
            departmentRepository.save(financeDepartment);
            
            marketingDepartment = Department.builder()
                    .name("Marketing Department")
                    .description("Promotes company products and services")
                    .build();
            departmentRepository.save(marketingDepartment);
            
            System.out.println("Default departments created");
        } else {
            List<Department> departments = departmentRepository.findAll();
            itDepartment = departments.get(0);
            hrDepartment = departments.size() > 1 ? departments.get(1) : itDepartment;
            financeDepartment = departments.size() > 2 ? departments.get(2) : itDepartment;
            marketingDepartment = departments.size() > 3 ? departments.get(3) : itDepartment;
        }

        // Create Admin user if not exists
        if (!userRepository.existsByEmail("admin@company.com")) {
            User admin = User.builder()
                    .staffId("ADMIN001")
                    .email("admin@company.com")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Administrator")
                    .role(Role.ADMIN)
                    .hourlyRate(new BigDecimal("100000.00"))
                    .baseSalary(new BigDecimal("20000000.00"))
                    .position(managerPosition)
                    .department(itDepartment)
                    .workShift(standardShift)
                    .leaveBalance(12)
                    .isActive(true)
                    .build();
            userRepository.save(admin);
            System.out.println("Default Admin user created: admin@company.com / admin123");
        }

        // Create Employee users if not exists (total 5+)
        if (!userRepository.existsByEmail("employee1@company.com")) {
            User employee1 = User.builder()
                    .staffId("EMP001")
                    .email("employee1@company.com")
                    .password(passwordEncoder.encode("employee123"))
                    .fullName("Nguyễn Văn A")
                    .role(Role.EMPLOYEE)
                    .hourlyRate(new BigDecimal("50000.00"))
                    .baseSalary(new BigDecimal("10000000.00"))
                    .position(devPosition)
                    .department(itDepartment)
                    .workShift(standardShift)
                    .leaveBalance(12)
                    .isActive(true)
                    .build();
            userRepository.save(employee1);
            System.out.println("Default Employee 1 created: employee1@company.com / employee123");
        }

        if (!userRepository.existsByEmail("employee2@company.com")) {
            User employee2 = User.builder()
                    .staffId("EMP002")
                    .email("employee2@company.com")
                    .password(passwordEncoder.encode("employee123"))
                    .fullName("Trần Thị B")
                    .role(Role.EMPLOYEE)
                    .hourlyRate(new BigDecimal("60000.00"))
                    .baseSalary(new BigDecimal("12000000.00"))
                    .position(designerPosition)
                    .department(itDepartment)
                    .workShift(standardShift)
                    .leaveBalance(10)
                    .isActive(true)
                    .build();
            userRepository.save(employee2);
            System.out.println("Default Employee 2 created: employee2@company.com / employee123");
        }

        if (!userRepository.existsByEmail("employee3@company.com")) {
            User employee3 = User.builder()
                    .staffId("EMP003")
                    .email("employee3@company.com")
                    .password(passwordEncoder.encode("employee123"))
                    .fullName("Lê Văn C")
                    .role(Role.EMPLOYEE)
                    .hourlyRate(new BigDecimal("55000.00"))
                    .baseSalary(new BigDecimal("11000000.00"))
                    .position(testerPosition)
                    .department(itDepartment)
                    .workShift(standardShift)
                    .leaveBalance(11)
                    .isActive(true)
                    .build();
            userRepository.save(employee3);
            System.out.println("Default Employee 3 created: employee3@company.com / employee123");
        }

        if (!userRepository.existsByEmail("employee4@company.com")) {
            User employee4 = User.builder()
                    .staffId("EMP004")
                    .email("employee4@company.com")
                    .password(passwordEncoder.encode("employee123"))
                    .fullName("Phạm Thị D")
                    .role(Role.EMPLOYEE)
                    .hourlyRate(new BigDecimal("58000.00"))
                    .baseSalary(new BigDecimal("11500000.00"))
                    .position(accountantPosition)
                    .department(financeDepartment)
                    .workShift(standardShift)
                    .leaveBalance(15)
                    .isActive(true)
                    .build();
            userRepository.save(employee4);
            System.out.println("Default Employee 4 created: employee4@company.com / employee123");
        }

        if (!userRepository.existsByEmail("employee5@company.com")) {
            User employee5 = User.builder()
                    .staffId("EMP005")
                    .email("employee5@company.com")
                    .password(passwordEncoder.encode("employee123"))
                    .fullName("Hoàng Văn E")
                    .role(Role.EMPLOYEE)
                    .hourlyRate(new BigDecimal("65000.00"))
                    .baseSalary(new BigDecimal("13000000.00"))
                    .position(managerPosition)
                    .department(marketingDepartment)
                    .workShift(standardShift)
                    .leaveBalance(13)
                    .isActive(true)
                    .build();
            userRepository.save(employee5);
            System.out.println("Default Employee 5 created: employee5@company.com / employee123");
        }

        if (!userRepository.existsByEmail("employee6@company.com")) {
            User employee6 = User.builder()
                    .staffId("EMP006")
                    .email("employee6@company.com")
                    .password(passwordEncoder.encode("employee123"))
                    .fullName("Đỗ Thị F")
                    .role(Role.EMPLOYEE)
                    .hourlyRate(new BigDecimal("52000.00"))
                    .baseSalary(new BigDecimal("10500000.00"))
                    .position(designerPosition)
                    .department(marketingDepartment)
                    .workShift(standardShift)
                    .leaveBalance(9)
                    .isActive(true)
                    .build();
            userRepository.save(employee6);
            System.out.println("Default Employee 6 created: employee6@company.com / employee123");
        }

        // Create sample data if attendance count == 0
        if (attendanceRepository.count() == 0) {
            // Get all created users
            List<User> users = userRepository.findAll();
            User admin = users.stream().filter(u -> u.getEmail().equals("admin@company.com")).findFirst().orElse(null);
            User emp1 = users.stream().filter(u -> u.getEmail().equals("employee1@company.com")).findFirst().orElse(null);
            User emp2 = users.stream().filter(u -> u.getEmail().equals("employee2@company.com")).findFirst().orElse(null);
            User emp3 = users.stream().filter(u -> u.getEmail().equals("employee3@company.com")).findFirst().orElse(null);
            User emp4 = users.stream().filter(u -> u.getEmail().equals("employee4@company.com")).findFirst().orElse(null);
            User emp5 = users.stream().filter(u -> u.getEmail().equals("employee5@company.com")).findFirst().orElse(null);
            User emp6 = users.stream().filter(u -> u.getEmail().equals("employee6@company.com")).findFirst().orElse(null);

            java.time.LocalDate today = java.time.LocalDate.now();

            // 1. Seed Leave Requests
            // Leave for emp6 (Do Thi F) approved in the past: 3 days
            LeaveRequest lr1 = LeaveRequest.builder()
                    .user(emp6)
                    .startDate(today.minusDays(15))
                    .endDate(today.minusDays(13))
                    .leaveType(LeaveType.ANNUAL)
                    .reason("Giải quyết công việc gia đình")
                    .status(RequestStatus.APPROVED)
                    .approvedBy(admin)
                    .approvedAt(java.time.LocalDateTime.now().minusDays(16))
                    .build();
            leaveRequestRepository.save(lr1);

            // Leave for emp2 (Tran Thi B) pending in the future: today + 5 to today + 7
            LeaveRequest lr2 = LeaveRequest.builder()
                    .user(emp2)
                    .startDate(today.plusDays(5))
                    .endDate(today.plusDays(7))
                    .leaveType(LeaveType.SICK)
                    .reason("Đi khám sức khỏe định kỳ")
                    .status(RequestStatus.PENDING)
                    .build();
            leaveRequestRepository.save(lr2);

            // Leave for emp3 (Le Van C) rejected in the past
            LeaveRequest lr3 = LeaveRequest.builder()
                    .user(emp3)
                    .startDate(today.minusDays(10))
                    .endDate(today.minusDays(9))
                    .leaveType(LeaveType.PERSONAL)
                    .reason("Nghỉ đi du lịch tự túc")
                    .status(RequestStatus.REJECTED)
                    .rejectionReason("Không thể phê duyệt do trùng lịch dự án quan trọng")
                    .approvedBy(admin)
                    .approvedAt(java.time.LocalDateTime.now().minusDays(11))
                    .build();
            leaveRequestRepository.save(lr3);

            // Leave for emp6 (Do Thi F) approved today: 1 day
            LeaveRequest lr4 = LeaveRequest.builder()
                    .user(emp6)
                    .startDate(today)
                    .endDate(today)
                    .leaveType(LeaveType.ANNUAL)
                    .reason("Giải quyết công việc cá nhân")
                    .status(RequestStatus.APPROVED)
                    .approvedBy(admin)
                    .approvedAt(java.time.LocalDateTime.now().minusDays(1))
                    .build();
            leaveRequestRepository.save(lr4);

            // 2. Seed Attendance records for the last 14 days and today
            for (int i = 14; i >= 0; i--) {
                java.time.LocalDate date = today.minusDays(i);
                java.time.DayOfWeek dow = date.getDayOfWeek();
                boolean isWeekend = dow == java.time.DayOfWeek.SATURDAY || dow == java.time.DayOfWeek.SUNDAY;

                if (isWeekend) {
                    // Weekend: seed a Saturday overtime shift for Le Van C
                    if (dow == java.time.DayOfWeek.SATURDAY) {
                        Attendance saturdayAtt = Attendance.builder()
                                .user(emp3)
                                .date(date)
                                .checkIn(java.time.LocalTime.of(8, 0))
                                .checkOut(java.time.LocalTime.of(12, 0))
                                .status(AttendanceStatus.PRESENT)
                                .hoursWorked(BigDecimal.valueOf(4.0))
                                .overtimeHours(BigDecimal.valueOf(4.0))
                                .notes("Trực dự án thứ Bảy")
                                .build();
                        attendanceRepository.save(saturdayAtt);
                    }
                    continue;
                }

                // emp1: Nguyen Van A - Always present on time
                Attendance att1 = Attendance.builder()
                        .user(emp1)
                        .date(date)
                        .checkIn(java.time.LocalTime.of(8, 0))
                        .checkOut(java.time.LocalTime.of(17, 0))
                        .status(AttendanceStatus.PRESENT)
                        .hoursWorked(BigDecimal.valueOf(8.0))
                        .overtimeHours(BigDecimal.ZERO)
                        .build();
                attendanceRepository.save(att1);

                // emp2: Tran Thi B - Sometimes late (excluding today)
                java.time.LocalTime checkIn2 = (i != 0 && i % 5 == 0) ? java.time.LocalTime.of(8, 35) : java.time.LocalTime.of(8, 5);
                AttendanceStatus status2 = (i != 0 && i % 5 == 0) ? AttendanceStatus.LATE : AttendanceStatus.PRESENT;
                Attendance att2 = Attendance.builder()
                        .user(emp2)
                        .date(date)
                        .checkIn(checkIn2)
                        .checkOut(java.time.LocalTime.of(17, 0))
                        .status(status2)
                        .hoursWorked(BigDecimal.valueOf(8.0))
                        .overtimeHours(BigDecimal.ZERO)
                        .build();
                attendanceRepository.save(att2);

                // emp3: Le Van C - Overtime works
                Attendance att3 = Attendance.builder()
                        .user(emp3)
                        .date(date)
                        .checkIn(java.time.LocalTime.of(7, 58))
                        .checkOut(java.time.LocalTime.of(19, 30))
                        .status(AttendanceStatus.PRESENT)
                        .hoursWorked(BigDecimal.valueOf(10.5))
                        .overtimeHours(BigDecimal.valueOf(2.5))
                        .notes("Tăng ca hỗ trợ vận hành dự án")
                        .build();
                attendanceRepository.save(att3);

                // emp4: Pham Thi D - Half day on some days (excluding today)
                java.time.LocalTime checkOut4 = (i != 0 && i % 7 == 0) ? java.time.LocalTime.of(12, 0) : java.time.LocalTime.of(17, 0);
                AttendanceStatus status4 = (i != 0 && i % 7 == 0) ? AttendanceStatus.HALF_DAY : AttendanceStatus.PRESENT;
                BigDecimal hours4 = (i != 0 && i % 7 == 0) ? BigDecimal.valueOf(4.0) : BigDecimal.valueOf(8.0);
                Attendance att4 = Attendance.builder()
                        .user(emp4)
                        .date(date)
                        .checkIn(java.time.LocalTime.of(8, 0))
                        .checkOut(checkOut4)
                        .status(status4)
                        .hoursWorked(hours4)
                        .overtimeHours(BigDecimal.ZERO)
                        .build();
                attendanceRepository.save(att4);

                // emp5: Hoang Van E - Absent on i = 0, 4, 9
                if (i == 0 || i == 4 || i == 9) {
                    Attendance att5 = Attendance.builder()
                            .user(emp5)
                            .date(date)
                            .status(AttendanceStatus.ABSENT)
                            .hoursWorked(BigDecimal.ZERO)
                            .overtimeHours(BigDecimal.ZERO)
                            .notes("Vắng mặt không phép")
                            .build();
                    attendanceRepository.save(att5);
                } else {
                    Attendance att5 = Attendance.builder()
                            .user(emp5)
                            .date(date)
                            .checkIn(java.time.LocalTime.of(8, 0))
                            .checkOut(java.time.LocalTime.of(17, 0))
                            .status(AttendanceStatus.PRESENT)
                            .hoursWorked(BigDecimal.valueOf(8.0))
                            .overtimeHours(BigDecimal.ZERO)
                            .build();
                    attendanceRepository.save(att5);
                }

                // emp6: Do Thi F - On approved leave for days 0, 13, 14, 15
                if (i == 0 || (i >= 13 && i <= 15)) {
                    Attendance att6 = Attendance.builder()
                            .user(emp6)
                            .date(date)
                            .status(AttendanceStatus.ON_LEAVE)
                            .hoursWorked(BigDecimal.ZERO)
                            .overtimeHours(BigDecimal.ZERO)
                            .notes("Nghỉ phép thường niên")
                            .build();
                    attendanceRepository.save(att6);
                } else {
                    Attendance att6 = Attendance.builder()
                            .user(emp6)
                            .date(date)
                            .checkIn(java.time.LocalTime.of(8, 0))
                            .checkOut(java.time.LocalTime.of(17, 0))
                            .status(AttendanceStatus.PRESENT)
                            .hoursWorked(BigDecimal.valueOf(8.0))
                            .overtimeHours(BigDecimal.ZERO)
                            .build();
                    attendanceRepository.save(att6);
                }
            }

            // 3. Seed Correction Requests
            java.time.LocalDate forgotDate = today.minusDays(5);
            Attendance forgotAtt = attendanceRepository.findByUserIdAndDate(emp2.getId(), forgotDate).orElse(null);
            if (forgotAtt != null) {
                forgotAtt.setCheckOut(null);
                forgotAtt.setStatus(AttendanceStatus.ERROR_DATA);
                forgotAtt.setHoursWorked(BigDecimal.ZERO);
                attendanceRepository.save(forgotAtt);

                CorrectionRequest cr1 = CorrectionRequest.builder()
                        .user(emp2)
                        .attendance(forgotAtt)
                        .date(forgotDate)
                        .checkIn(java.time.LocalTime.of(8, 5))
                        .checkOut(java.time.LocalTime.of(17, 0))
                        .reason("Hôm đó tôi quên quẹt thẻ lúc ra về")
                        .status(RequestStatus.PENDING)
                        .build();
                correctionRequestRepository.save(cr1);
            }

            java.time.LocalDate correctDate = today.minusDays(12);
            Attendance correctAtt = attendanceRepository.findByUserIdAndDate(emp3.getId(), correctDate).orElse(null);
            if (correctAtt != null) {
                CorrectionRequest cr2 = CorrectionRequest.builder()
                        .user(emp3)
                        .attendance(correctAtt)
                        .date(correctDate)
                        .checkIn(java.time.LocalTime.of(8, 0))
                        .checkOut(java.time.LocalTime.of(19, 30))
                        .reason("Lỗi thiết bị chấm công đầu vào")
                        .status(RequestStatus.APPROVED)
                        .approvedBy(admin)
                        .approvedAt(java.time.LocalDateTime.now().minusDays(11))
                        .build();
                correctionRequestRepository.save(cr2);
            }

            // 4. Seed Payroll History for April & May 2026
            int prevMonth = today.minusMonths(1).getMonthValue();
            int prevYear = today.minusMonths(1).getYear();
            int prevMonth2 = today.minusMonths(2).getMonthValue();
            int prevYear2 = today.minusMonths(2).getYear();

            for (User u : users) {
                if (u.getRole() == Role.ADMIN) continue;

                // April Payroll
                Payroll pApril = Payroll.builder()
                        .user(u)
                        .monthNum(prevMonth2)
                        .yearNum(prevYear2)
                        .baseSalary(u.getBaseSalary())
                        .workingDays(22)
                        .absentDays(0)
                        .totalHours(BigDecimal.valueOf(176.0))
                        .overtimeHours(BigDecimal.valueOf(10.0))
                        .overtimePay(BigDecimal.valueOf(10.0).multiply(u.getHourlyRate()).multiply(BigDecimal.valueOf(1.5)))
                        .allowances(BigDecimal.valueOf(500000.00))
                        .deductions(BigDecimal.valueOf(100000.00))
                        .insurance(u.getBaseSalary().multiply(BigDecimal.valueOf(0.105)))
                        .tax(u.getBaseSalary().multiply(BigDecimal.valueOf(0.05)))
                        .netSalary(u.getBaseSalary()
                                .add(BigDecimal.valueOf(10.0).multiply(u.getHourlyRate()).multiply(BigDecimal.valueOf(1.5)))
                                .add(BigDecimal.valueOf(500000.00))
                                .subtract(BigDecimal.valueOf(100000.00))
                                .subtract(u.getBaseSalary().multiply(BigDecimal.valueOf(0.105)))
                                .subtract(u.getBaseSalary().multiply(BigDecimal.valueOf(0.05))))
                        .status(PayrollStatus.PAID)
                        .isPaid(true)
                        .paidAt(java.time.LocalDateTime.of(prevYear2, prevMonth2, 5, 9, 0))
                        .notes("Đã thanh toán chuyển khoản qua ngân hàng")
                        .build();
                payrollRepository.save(pApril);

                // May Payroll
                Payroll pMay = Payroll.builder()
                        .user(u)
                        .monthNum(prevMonth)
                        .yearNum(prevYear)
                        .baseSalary(u.getBaseSalary())
                        .workingDays(21)
                        .absentDays(u.getEmail().equals("employee5@company.com") ? 2 : 0)
                        .totalHours(BigDecimal.valueOf(168.0))
                        .overtimeHours(BigDecimal.valueOf(5.0))
                        .overtimePay(BigDecimal.valueOf(5.0).multiply(u.getHourlyRate()).multiply(BigDecimal.valueOf(1.5)))
                        .allowances(BigDecimal.valueOf(500000.00))
                        .deductions(BigDecimal.valueOf(100000.00))
                        .insurance(u.getBaseSalary().multiply(BigDecimal.valueOf(0.105)))
                        .tax(u.getBaseSalary().multiply(BigDecimal.valueOf(0.05)))
                        .netSalary(u.getBaseSalary()
                                .add(BigDecimal.valueOf(5.0).multiply(u.getHourlyRate()).multiply(BigDecimal.valueOf(1.5)))
                                .add(BigDecimal.valueOf(500000.00))
                                .subtract(BigDecimal.valueOf(100000.00))
                                .subtract(u.getBaseSalary().multiply(BigDecimal.valueOf(0.105)))
                                .subtract(u.getBaseSalary().multiply(BigDecimal.valueOf(0.05))))
                        .status(PayrollStatus.PAID)
                        .isPaid(true)
                        .paidAt(java.time.LocalDateTime.of(prevYear, prevMonth, 5, 9, 0))
                        .notes("Đã thanh toán lương tháng " + prevMonth)
                        .build();
                payrollRepository.save(pMay);
            }
            System.out.println("Sample attendance, leave requests, correction requests, and payrolls seeded successfully.");
        }
    }
}
