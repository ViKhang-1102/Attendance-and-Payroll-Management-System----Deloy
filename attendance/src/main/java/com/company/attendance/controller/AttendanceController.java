
package com.company.attendance.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.company.attendance.dto.attendance.AttendanceCreateDTO;
import com.company.attendance.dto.attendance.AttendanceResponseDTO;
import com.company.attendance.dto.attendance.AttendanceUpdateDTO;
import com.company.attendance.dto.attendance.TodayStatsDTO;
import com.company.attendance.model.entity.User;
import com.company.attendance.repository.UserRepository;
import com.company.attendance.service.AttendanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
@Tag(name = "Attendance Management", description = "APIs for managing attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;

    // ==================== ADMIN ENDPOINTS ====================
    @Operation(summary = "Get statistics for a specific date (Admin only)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved statistics")
    @GetMapping("/today-stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TodayStatsDTO> getTodayStats(
            @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(attendanceService.getTodayStats(date));
    }

    @Operation(summary = "Get all attendances (Admin only)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all attendances")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AttendanceResponseDTO>> getAllAttendances() {
        return ResponseEntity.ok(attendanceService.getAllAttendances());
    }

    @Operation(summary = "Get attendance by ID (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved attendance"),
            @ApiResponse(responseCode = "404", description = "Attendance not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AttendanceResponseDTO> getAttendanceById(@PathVariable UUID id) {
        return ResponseEntity.ok(attendanceService.getAttendanceById(id));
    }

    @Operation(summary = "Get attendances by user ID (Admin only)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved attendances")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendancesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(attendanceService.getAttendancesByUserId(userId));
    }

    @Operation(summary = "Get attendances by date range (Admin only)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved attendances")
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendancesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getAttendancesByDateRange(startDate, endDate));
    }

    @Operation(summary = "Get attendances by user ID and date range (Admin only)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved attendances")
    @GetMapping("/user/{userId}/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendancesByUserIdAndDateRange(
            @PathVariable UUID userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getAttendancesByUserIdAndDateRange(userId, startDate, endDate));
    }

    @Operation(summary = "Create new attendance (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created attendance"),
            @ApiResponse(responseCode = "400", description = "Invalid input or attendance already exists for this date"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AttendanceResponseDTO> createAttendance(@Valid @RequestBody AttendanceCreateDTO attendanceCreateDTO) {
        AttendanceResponseDTO created = attendanceService.createAttendance(attendanceCreateDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Update attendance (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated attendance"),
            @ApiResponse(responseCode = "404", description = "Attendance not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AttendanceResponseDTO> updateAttendance(
            @PathVariable UUID id,
            @Valid @RequestBody AttendanceUpdateDTO attendanceUpdateDTO) {
        return ResponseEntity.ok(attendanceService.updateAttendance(id, attendanceUpdateDTO));
    }

    @Operation(summary = "Delete attendance (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted attendance"),
            @ApiResponse(responseCode = "404", description = "Attendance not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAttendance(@PathVariable UUID id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== EMPLOYEE ENDPOINTS ====================
    @Operation(summary = "Get current employee's own attendances")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved attendances")
    @GetMapping("/my-attendances")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<AttendanceResponseDTO>> getMyAttendances(Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(attendanceService.getAttendancesByUserId(userId));
    }

    @Operation(summary = "Get current employee's attendances by date range")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved attendances")
    @GetMapping("/my-attendances/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<AttendanceResponseDTO>> getMyAttendancesByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(attendanceService.getAttendancesByUserIdAndDateRange(userId, startDate, endDate));
    }

    @Operation(summary = "Check In/Out for current employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully checked in/out"),
            @ApiResponse(responseCode = "400", description = "Already checked in/out")
    })
    @PostMapping("/check-in-out")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<AttendanceResponseDTO> checkInOut(
            @RequestParam(required = false) String notes,
            Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(attendanceService.checkInOut(userId, notes));
    }

    // Helper method to get current user ID from authentication
    private UUID getCurrentUserId(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));
        return user.getId();
    }
}

