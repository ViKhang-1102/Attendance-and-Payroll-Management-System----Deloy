
package com.company.attendance.controller;

import com.company.attendance.dto.payrollconfig.PayrollConfigCreateDTO;
import com.company.attendance.dto.payrollconfig.PayrollConfigResponseDTO;
import com.company.attendance.dto.payrollconfig.PayrollConfigUpdateDTO;
import com.company.attendance.service.PayrollConfigService;
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
@RequestMapping("/api/payroll-configs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Payroll Config Management", description = "APIs for payroll configs")
public class PayrollConfigController {
    private final PayrollConfigService payrollConfigService;

    @Operation(summary = "Get all payroll configs")
    @GetMapping
    public ResponseEntity<List<PayrollConfigResponseDTO>> getAllPayrollConfigs() {
        return ResponseEntity.ok(payrollConfigService.getAllPayrollConfigs());
    }

    @Operation(summary = "Get active payroll config")
    @GetMapping("/active")
    public ResponseEntity<PayrollConfigResponseDTO> getActivePayrollConfig() {
        return ResponseEntity.ok(payrollConfigService.getActivePayrollConfig());
    }

    @Operation(summary = "Create payroll config (auto-activates it)")
    @PostMapping
    public ResponseEntity<PayrollConfigResponseDTO> createPayrollConfig(@Valid @RequestBody PayrollConfigCreateDTO dto) {
        return new ResponseEntity<>(payrollConfigService.createPayrollConfig(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update payroll config")
    @PutMapping("/{id}")
    public ResponseEntity<PayrollConfigResponseDTO> updatePayrollConfig(@PathVariable UUID id, @Valid @RequestBody PayrollConfigUpdateDTO dto) {
        return ResponseEntity.ok(payrollConfigService.updatePayrollConfig(id, dto));
    }

    @Operation(summary = "Set payroll config as active")
    @PostMapping("/{id}/set-active")
    public ResponseEntity<PayrollConfigResponseDTO> setActive(@PathVariable UUID id) {
        return ResponseEntity.ok(payrollConfigService.setActive(id));
    }
}
