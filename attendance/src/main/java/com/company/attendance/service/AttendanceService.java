
package com.company.attendance.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.company.attendance.dto.attendance.AttendanceCreateDTO;
import com.company.attendance.dto.attendance.AttendanceResponseDTO;
import com.company.attendance.dto.attendance.AttendanceUpdateDTO;
import com.company.attendance.dto.attendance.TodayStatsDTO;
import com.company.attendance.mapper.AttendanceMapper;
import com.company.attendance.model.entity.Attendance;
import com.company.attendance.model.entity.LeaveRequest;
import com.company.attendance.model.entity.User;
import com.company.attendance.model.entity.WorkShift;
import com.company.attendance.model.enums.AttendanceStatus;
import com.company.attendance.model.enums.RequestStatus;
import com.company.attendance.repository.AttendanceRepository;
import com.company.attendance.repository.LeaveRequestRepository;
import com.company.attendance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final AttendanceMapper attendanceMapper;
    private final LeaveRequestRepository leaveRequestRepository;

    private static final BigDecimal REGULAR_HOURS = BigDecimal.valueOf(8);

    // Lấy tất cả attendance (Admin)
    public List<AttendanceResponseDTO> getAllAttendances() {
        return attendanceRepository.findAll()
                .stream()
                .map(attendanceMapper::toAttendanceResponseDTO)
                .toList();
    }

    // Lấy attendance theo ID
    public AttendanceResponseDTO getAttendanceById(UUID id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not found"));
        return attendanceMapper.toAttendanceResponseDTO(attendance);
    }

    // Lấy attendance theo userId (Admin & Employee)
    public List<AttendanceResponseDTO> getAttendancesByUserId(UUID userId) {
        return attendanceRepository.findByUserId(userId)
                .stream()
                .map(attendanceMapper::toAttendanceResponseDTO)
                .toList();
    }

    // Lấy attendance theo userId và date range
    public List<AttendanceResponseDTO> getAttendancesByUserIdAndDateRange(
            UUID userId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByUserIdAndDateRange(userId, startDate, endDate)
                .stream()
                .map(attendanceMapper::toAttendanceResponseDTO)
                .toList();
    }

    // Lấy attendance theo date range (Admin)
    public List<AttendanceResponseDTO> getAttendancesByDateRange(
            LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByDateRange(startDate, endDate)
                .stream()
                .map(attendanceMapper::toAttendanceResponseDTO)
                .toList();
    }

    // Tạo attendance mới (Admin)
    public AttendanceResponseDTO createAttendance(AttendanceCreateDTO attendanceCreateDTO) {
        User user = userRepository.findById(attendanceCreateDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (attendanceRepository.findByUserIdAndDate(
                attendanceCreateDTO.getUserId(), attendanceCreateDTO.getDate()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Attendance already exists for this user on this date");
        }

        Attendance attendance = attendanceMapper.toAttendance(attendanceCreateDTO, user);
        calculateHours(attendance, user.getWorkShift());
        attendance = attendanceRepository.save(attendance);
        return attendanceMapper.toAttendanceResponseDTO(attendance);
    }

    // Cập nhật attendance (Admin)
    public AttendanceResponseDTO updateAttendance(UUID id, AttendanceUpdateDTO attendanceUpdateDTO) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not found"));
        User user = attendance.getUser();
        attendanceMapper.updateAttendanceFromDTO(attendanceUpdateDTO, attendance);
        calculateHours(attendance, user.getWorkShift());
        attendance = attendanceRepository.save(attendance);
        return attendanceMapper.toAttendanceResponseDTO(attendance);
    }

    // Xóa attendance (Admin)
    public void deleteAttendance(UUID id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not found"));
        attendanceRepository.delete(attendance);
    }

    // Check In/Out for employee
    public AttendanceResponseDTO checkInOut(UUID userId, String notes) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Attendance attendance = attendanceRepository.findByUserIdAndDate(userId, today)
                .orElseGet(() -> Attendance.builder()
                        .user(user)
                        .date(today)
                        .status(AttendanceStatus.PRESENT)
                        .notes(notes)
                        .build());

        if (attendance.getCheckIn() == null) {
            // Check In
            attendance.setCheckIn(now);
            // Check if late using user's work shift
            WorkShift shift = user.getWorkShift();
            if (shift != null) {
                LocalTime shiftStart = shift.getCheckInTime();
                // Add grace minutes to shift start
                LocalTime latestAllowed = shiftStart.plusMinutes(shift.getGraceMinutes());
                if (now.isAfter(latestAllowed)) {
                    attendance.setStatus(AttendanceStatus.LATE);
                }
            }
        } else if (attendance.getCheckOut() == null) {
            // Check Out
            attendance.setCheckOut(now);
            calculateHours(attendance, user.getWorkShift());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already checked in and out today");
        }

        attendance = attendanceRepository.save(attendance);
        return attendanceMapper.toAttendanceResponseDTO(attendance);
    }

    // Get statistics for a specific date (default to today)
    public TodayStatsDTO getTodayStats(LocalDate date) {
        LocalDate today = (date != null) ? date : LocalDate.now();
        List<User> allEmployees = userRepository.findAll().stream()
                .filter(u -> u.getRole() != com.company.attendance.model.enums.Role.ADMIN)
                .toList();
        long totalEmployees = allEmployees.size();
        long presentCount = 0;
        long absentCount = 0;
        long leaveCount = 0;
        long errorDataCount = 0;

        for (User user : allEmployees) {
            Attendance attendance = attendanceRepository.findByUserIdAndDate(user.getId(), today).orElse(null);
            if (attendance != null) {
                if (attendance.getStatus() == AttendanceStatus.PRESENT || attendance.getStatus() == AttendanceStatus.LATE) {
                    presentCount++;
                } else if (attendance.getStatus() == AttendanceStatus.ABSENT) {
                    absentCount++;
                } else if (attendance.getStatus() == AttendanceStatus.ON_LEAVE) {
                    leaveCount++;
                } else if (attendance.getStatus() == AttendanceStatus.ERROR_DATA) {
                    errorDataCount++;
                }
            } else {
                // Check if user is on approved leave today
                List<LeaveRequest> userLeaves = leaveRequestRepository.findByUserId(user.getId());
                boolean isOnLeave = userLeaves.stream()
                        .anyMatch(lr -> lr.getStatus() == RequestStatus.APPROVED
                                && !today.isBefore(lr.getStartDate())
                                && !today.isAfter(lr.getEndDate()));
                if (isOnLeave) {
                    leaveCount++;
                    // Create ON_LEAVE attendance record
                    Attendance leaveAttendance = Attendance.builder()
                            .user(user)
                            .date(today)
                            .status(AttendanceStatus.ON_LEAVE)
                            .build();
                    attendanceRepository.save(leaveAttendance);
                } else {
                    absentCount++;
                    // Create ABSENT attendance record
                    Attendance absentAttendance = Attendance.builder()
                            .user(user)
                            .date(today)
                            .status(AttendanceStatus.ABSENT)
                            .build();
                    attendanceRepository.save(absentAttendance);
                }
            }
        }

        return TodayStatsDTO.builder()
                .totalEmployees(totalEmployees)
                .presentCount(presentCount)
                .absentCount(absentCount + errorDataCount)
                .leaveCount(leaveCount)
                .build();
    }

    // Helper method để tính hoursWorked và overtimeHours
    private void calculateHours(Attendance attendance, WorkShift shift) {
        LocalTime checkIn = attendance.getCheckIn();
        LocalTime checkOut = attendance.getCheckOut();

        if (checkIn == null || checkOut == null || checkOut.isBefore(checkIn)) {
            attendance.setHoursWorked(BigDecimal.ZERO);
            attendance.setOvertimeHours(BigDecimal.ZERO);
            attendance.setStatus(AttendanceStatus.ERROR_DATA);
            return;
        }

        Duration duration = Duration.between(checkIn, checkOut);
        double totalMinutes = duration.toMinutes();
        BigDecimal totalHours = BigDecimal.valueOf(totalMinutes / 60.0)
                .setScale(2, RoundingMode.HALF_UP);

        attendance.setHoursWorked(totalHours);

        if (totalHours.compareTo(REGULAR_HOURS) > 0) {
            attendance.setOvertimeHours(totalHours.subtract(REGULAR_HOURS));
        } else {
            attendance.setOvertimeHours(BigDecimal.ZERO);
        }
    }
}
