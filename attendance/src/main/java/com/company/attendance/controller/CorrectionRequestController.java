
package com.company.attendance.controller;

import com.company.attendance.dto.correctionrequest.CorrectionRequestCreateDTO;
import com.company.attendance.dto.correctionrequest.CorrectionRequestResponseDTO;
import com.company.attendance.service.CorrectionRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/correction-requests")
@RequiredArgsConstructor
@Tag(name = "CorrectionRequest Management", description = "APIs for correction requests")
public class CorrectionRequestController {
    private final CorrectionRequestService correctionRequestService;

    @Operation(summary = "Get all correction requests (admin)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CorrectionRequestResponseDTO>> getAllCorrectionRequests() {
        return ResponseEntity.ok(correctionRequestService.getAllCorrectionRequests());
    }

    @Operation(summary = "Get my correction requests (employee)")
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<CorrectionRequestResponseDTO>> getMyCorrectionRequests() {
        return ResponseEntity.ok(correctionRequestService.getMyCorrectionRequests());
    }

    @Operation(summary = "Create correction request (employee)")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<CorrectionRequestResponseDTO> createCorrectionRequest(@Valid @RequestBody CorrectionRequestCreateDTO dto) {
        return new ResponseEntity<>(correctionRequestService.createCorrectionRequest(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Approve correction request (admin)")
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CorrectionRequestResponseDTO> approveCorrectionRequest(@PathVariable UUID id, @RequestParam(required = false) String adminNotes) {
        return ResponseEntity.ok(correctionRequestService.approveCorrectionRequest(id, adminNotes));
    }

    @Operation(summary = "Reject correction request (admin)")
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CorrectionRequestResponseDTO> rejectCorrectionRequest(@PathVariable UUID id, @RequestParam String rejectionReason) {
        return ResponseEntity.ok(correctionRequestService.rejectCorrectionRequest(id, rejectionReason));
    }
}
