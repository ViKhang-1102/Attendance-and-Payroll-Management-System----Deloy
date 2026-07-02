
package com.company.attendance.service;

import com.company.attendance.dto.leaverequest.LeaveRequestCreateDTO;
import com.company.attendance.dto.leaverequest.LeaveRequestResponseDTO;
import com.company.attendance.dto.leaverequest.LeaveRequestUpdateDTO;
import com.company.attendance.mapper.LeaveRequestMapper;
import com.company.attendance.model.entity.LeaveRequest;
import com.company.attendance.model.entity.User;
import com.company.attendance.model.enums.RequestStatus;
import com.company.attendance.repository.LeaveRequestRepository;
import com.company.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final LeaveRequestMapper leaveRequestMapper;

    // ==================== ADMIN METHODS ====================

    public List<LeaveRequestResponseDTO> getAllLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .map(leaveRequestMapper::toResponseDTO)
                .toList();
    }

    public LeaveRequestResponseDTO getLeaveRequestById(UUID id) {
        return leaveRequestRepository.findById(id)
                .map(leaveRequestMapper::toResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave request not found"));
    }

    public List<LeaveRequestResponseDTO> getLeaveRequestsByUserId(UUID userId) {
        return leaveRequestRepository.findByUserId(userId).stream()
                .map(leaveRequestMapper::toResponseDTO)
                .toList();
    }

    public LeaveRequestResponseDTO approveLeaveRequest(UUID id, UUID adminId, Boolean approve, String rejectionReason) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave request not found"));

        if (leaveRequest.getStatus() != RequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leave request is already processed");
        }

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));

        leaveRequest.setStatus(approve ? RequestStatus.APPROVED : RequestStatus.REJECTED);
        leaveRequest.setApprovedBy(admin);
        leaveRequest.setApprovedAt(LocalDateTime.now());
        if (!approve) {
            leaveRequest.setRejectionReason(rejectionReason);
        }

        // If approved, deduct leave balance
        if (approve) {
            User user = leaveRequest.getUser();
            long days = leaveRequest.getEndDate().toEpochDay() - leaveRequest.getStartDate().toEpochDay() + 1;
            user.setLeaveBalance(user.getLeaveBalance() - (int) days);
            userRepository.save(user);
        }

        return leaveRequestMapper.toResponseDTO(leaveRequestRepository.save(leaveRequest));
    }

    // ==================== EMPLOYEE METHODS ====================

    public List<LeaveRequestResponseDTO> getMyLeaveRequests(UUID userId) {
        return leaveRequestRepository.findByUserId(userId).stream()
                .map(leaveRequestMapper::toResponseDTO)
                .toList();
    }

    public LeaveRequestResponseDTO createLeaveRequest(UUID userId, LeaveRequestCreateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Validate dates
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date must be after start date");
        }

        // Check leave balance
        long days = dto.getEndDate().toEpochDay() - dto.getStartDate().toEpochDay() + 1;
        if (user.getLeaveBalance() < days) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient leave balance");
        }

        LeaveRequest leaveRequest = leaveRequestMapper.toEntity(dto);
        leaveRequest.setUser(user);
        leaveRequest.setStatus(RequestStatus.PENDING);

        return leaveRequestMapper.toResponseDTO(leaveRequestRepository.save(leaveRequest));
    }

    public LeaveRequestResponseDTO updateLeaveRequest(UUID id, UUID userId, LeaveRequestUpdateDTO dto) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave request not found"));

        // Check ownership and status
        if (!leaveRequest.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own leave requests");
        }
        if (leaveRequest.getStatus() != RequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can only update pending leave requests");
        }

        leaveRequestMapper.updateEntityFromDTO(dto, leaveRequest);
        return leaveRequestMapper.toResponseDTO(leaveRequestRepository.save(leaveRequest));
    }

    public void deleteLeaveRequest(UUID id, UUID userId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave request not found"));

        if (!leaveRequest.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own leave requests");
        }
        if (leaveRequest.getStatus() != RequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can only delete pending leave requests");
        }

        leaveRequestRepository.delete(leaveRequest);
    }
}

