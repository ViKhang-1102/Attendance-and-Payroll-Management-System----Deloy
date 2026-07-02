
package com.company.attendance.controller;

import com.company.attendance.dto.leaverequest.LeaveRequestApproveDTO;
import com.company.attendance.dto.leaverequest.LeaveRequestCreateDTO;
import com.company.attendance.dto.leaverequest.LeaveRequestResponseDTO;
import com.company.attendance.dto.leaverequest.LeaveRequestUpdateDTO;
import com.company.attendance.model.entity.User;
import com.company.attendance.repository.UserRepository;
import com.company.attendance.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
@Tag(name = "Leave Request Management", description = "APIs for managing leave requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final UserRepository userRepository;

    // ==================== ADMIN ENDPOINTS ====================
    @Operation(summary = "Get all leave requests (Admin only)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all leave requests")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LeaveRequestResponseDTO>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }

    @Operation(summary = "Get leave request by ID (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved leave request"),
            @ApiResponse(responseCode = "404", description = "Leave request not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LeaveRequestResponseDTO> getLeaveRequestById(@PathVariable UUID id) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestById(id));
    }

    @Operation(summary = "Get leave requests by user ID (Admin only)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved leave requests")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LeaveRequestResponseDTO>> getLeaveRequestsByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestsByUserId(userId));
    }

    @Operation(summary = "Approve or reject leave request (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed leave request"),
            @ApiResponse(responseCode = "404", description = "Leave request not found")
    })
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LeaveRequestResponseDTO> approveLeaveRequest(
            @PathVariable UUID id,
            @RequestParam Boolean approve,
            @RequestBody(required = false) LeaveRequestApproveDTO dto,
            Authentication authentication) {
        UUID adminId = getCurrentUserId(authentication);
        String rejectionReason = (dto != null) ? dto.getRejectionReason() : null;
        return ResponseEntity.ok(leaveRequestService.approveLeaveRequest(id, adminId, approve, rejectionReason));
    }

    // ==================== EMPLOYEE ENDPOINTS ====================
    @Operation(summary = "Get current employee's own leave requests")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved leave requests")
    @GetMapping("/my-requests")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<LeaveRequestResponseDTO>> getMyLeaveRequests(Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(leaveRequestService.getMyLeaveRequests(userId));
    }

    @Operation(summary = "Create new leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created leave request"),
            @ApiResponse(responseCode = "400", description = "Invalid input or insufficient leave balance"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<LeaveRequestResponseDTO> createLeaveRequest(
            @Valid @RequestBody LeaveRequestCreateDTO dto,
            Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        LeaveRequestResponseDTO created = leaveRequestService.createLeaveRequest(userId, dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Update pending leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated leave request"),
            @ApiResponse(responseCode = "404", description = "Leave request not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<LeaveRequestResponseDTO> updateLeaveRequest(
            @PathVariable UUID id,
            @Valid @RequestBody LeaveRequestUpdateDTO dto,
            Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(leaveRequestService.updateLeaveRequest(id, userId, dto));
    }

    @Operation(summary = "Delete pending leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted leave request"),
            @ApiResponse(responseCode = "404", description = "Leave request not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Void> deleteLeaveRequest(
            @PathVariable UUID id,
            Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        leaveRequestService.deleteLeaveRequest(id, userId);
        return ResponseEntity.noContent().build();
    }

    // Helper method
    private UUID getCurrentUserId(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));
        return user.getId();
    }
}

