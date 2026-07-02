
package com.company.attendance.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.company.attendance.dto.correctionrequest.CorrectionRequestCreateDTO;
import com.company.attendance.dto.correctionrequest.CorrectionRequestResponseDTO;
import com.company.attendance.mapper.CorrectionRequestMapper;
import com.company.attendance.model.entity.Attendance;
import com.company.attendance.model.entity.CorrectionRequest;
import com.company.attendance.model.entity.User;
import com.company.attendance.model.enums.AttendanceStatus;
import com.company.attendance.model.enums.RequestStatus;
import com.company.attendance.repository.AttendanceRepository;
import com.company.attendance.repository.CorrectionRequestRepository;
import com.company.attendance.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CorrectionRequestService {
    private final CorrectionRequestRepository correctionRequestRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final CorrectionRequestMapper correctionRequestMapper;

    public List<CorrectionRequestResponseDTO> getAllCorrectionRequests() {
        return correctionRequestRepository.findAll()
                .stream()
                .map(correctionRequestMapper::toResponseDTO)
                .toList();
    }

    public List<CorrectionRequestResponseDTO> getMyCorrectionRequests() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return correctionRequestRepository.findByUser(user)
                .stream()
                .map(correctionRequestMapper::toResponseDTO)
                .toList();
    }

    public CorrectionRequestResponseDTO createCorrectionRequest(CorrectionRequestCreateDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Attendance attendance = attendanceRepository.findById(dto.getAttendanceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not found"));

        if (!attendance.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only create correction requests for your own attendances");
        }

        CorrectionRequest request = correctionRequestMapper.toEntity(dto);
        request.setUser(user);
        request.setAttendance(attendance);
        request.setDate(attendance.getDate());
        request.setStatus(RequestStatus.PENDING);

        request = correctionRequestRepository.save(request);
        return correctionRequestMapper.toResponseDTO(request);
    }

    public CorrectionRequestResponseDTO approveCorrectionRequest(UUID id, String adminNotes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        CorrectionRequest request = correctionRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CorrectionRequest not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CorrectionRequest is not pending");
        }

        Attendance attendance = request.getAttendance();
        if (request.getCheckIn() != null) {
            attendance.setCheckIn(request.getCheckIn());
        }
        if (request.getCheckOut() != null) {
            attendance.setCheckOut(request.getCheckOut());
        }

        attendance.setStatus(AttendanceStatus.PRESENT);
        attendance = attendanceRepository.save(attendance);

        request.setStatus(RequestStatus.APPROVED);
        request.setApprovedBy(admin);
        request.setApprovedAt(LocalDateTime.now());
        request = correctionRequestRepository.save(request);

        return correctionRequestMapper.toResponseDTO(request);
    }

    public CorrectionRequestResponseDTO rejectCorrectionRequest(UUID id, String rejectionReason) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User admin = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        CorrectionRequest request = correctionRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CorrectionRequest not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CorrectionRequest is not pending");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(rejectionReason);
        request.setApprovedBy(admin);
        request.setApprovedAt(LocalDateTime.now());
        request = correctionRequestRepository.save(request);

        return correctionRequestMapper.toResponseDTO(request);
    }
}
