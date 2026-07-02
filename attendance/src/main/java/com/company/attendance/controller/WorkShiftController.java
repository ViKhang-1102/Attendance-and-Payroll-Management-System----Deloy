
package com.company.attendance.controller;

import com.company.attendance.dto.workshift.WorkShiftCreateDTO;
import com.company.attendance.dto.workshift.WorkShiftResponseDTO;
import com.company.attendance.dto.workshift.WorkShiftUpdateDTO;
import com.company.attendance.service.WorkShiftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/api/work-shifts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Work Shift Management", description = "APIs for managing work shifts (Admin only)")
public class WorkShiftController {
    private final WorkShiftService workShiftService;

    @Operation(summary = "Get all work shifts")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved work shifts")
    @GetMapping
    public ResponseEntity<List<WorkShiftResponseDTO>> getAllWorkShifts() {
        return ResponseEntity.ok(workShiftService.getAllWorkShifts());
    }

    @Operation(summary = "Get work shift by ID")
    @GetMapping("/{id}")
    public ResponseEntity<WorkShiftResponseDTO> getWorkShiftById(@PathVariable UUID id) {
        return ResponseEntity.ok(workShiftService.getWorkShiftById(id));
    }

    @Operation(summary = "Create work shift")
    @PostMapping
    public ResponseEntity<WorkShiftResponseDTO> createWorkShift(@Valid @RequestBody WorkShiftCreateDTO workShiftCreateDTO) {
        return new ResponseEntity<>(workShiftService.createWorkShift(workShiftCreateDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Update work shift")
    @PutMapping("/{id}")
    public ResponseEntity<WorkShiftResponseDTO> updateWorkShift(@PathVariable UUID id, @Valid @RequestBody WorkShiftUpdateDTO workShiftUpdateDTO) {
        return ResponseEntity.ok(workShiftService.updateWorkShift(id, workShiftUpdateDTO));
    }

    @Operation(summary = "Delete work shift")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkShift(@PathVariable UUID id) {
        workShiftService.deleteWorkShift(id);
        return ResponseEntity.noContent().build();
    }
}
