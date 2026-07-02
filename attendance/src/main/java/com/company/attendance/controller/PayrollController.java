
package com.company.attendance.controller;

import com.company.attendance.dto.payroll.PayrollCreateDTO;
import com.company.attendance.dto.payroll.PayrollResponseDTO;
import com.company.attendance.dto.payroll.PayrollUpdateDTO;
import com.company.attendance.model.entity.User;
import com.company.attendance.repository.UserRepository;
import com.company.attendance.service.PayrollService;
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
@RequestMapping("/api/payrolls")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Payroll Management", description = "APIs for managing payroll (Admin only)")
public class PayrollController {

    private final PayrollService payrollService;
    private final UserRepository userRepository;

    // ==================== GET ENDPOINTS ====================
    @Operation(summary = "Get all payrolls")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all payrolls")
    @GetMapping
    public ResponseEntity<List<PayrollResponseDTO>> getAllPayrolls() {
        return ResponseEntity.ok(payrollService.getAllPayrolls());
    }

    @Operation(summary = "Get payroll by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved payroll"),
            @ApiResponse(responseCode = "404", description = "Payroll not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PayrollResponseDTO> getPayrollById(@PathVariable UUID id) {
        return ResponseEntity.ok(payrollService.getPayrollById(id));
    }

    @Operation(summary = "Get payrolls by user ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved payrolls")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PayrollResponseDTO>> getPayrollsByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(payrollService.getPayrollsByUserId(userId));
    }

    @Operation(summary = "Get payrolls by month and year")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved payrolls")
    @GetMapping("/month-year")
    public ResponseEntity<List<PayrollResponseDTO>> getPayrollsByMonthAndYear(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(payrollService.getPayrollsByMonthAndYear(month, year));
    }

    // ==================== GENERATE PAYROLL ENDPOINTS ====================
    @Operation(summary = "Generate payroll for a single user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully generated payroll"),
            @ApiResponse(responseCode = "400", description = "Invalid input or payroll already exists"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/generate/user")
    public ResponseEntity<PayrollResponseDTO> generatePayrollForUser(
            @Valid @RequestBody PayrollCreateDTO payrollCreateDTO) {
        PayrollResponseDTO created = payrollService.generatePayrollForUser(payrollCreateDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Generate payroll for all employees in a month/year")
    @ApiResponse(responseCode = "200", description = "Successfully generated payrolls")
    @PostMapping("/generate/all")
    public ResponseEntity<List<PayrollResponseDTO>> generatePayrollForAllEmployees(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(payrollService.generatePayrollForAllEmployees(month, year));
    }

    // ==================== UPDATE & DELETE ====================
    @Operation(summary = "Update payroll")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated payroll"),
            @ApiResponse(responseCode = "404", description = "Payroll not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PayrollResponseDTO> updatePayroll(
            @PathVariable UUID id,
            @Valid @RequestBody PayrollUpdateDTO payrollUpdateDTO) {
        return ResponseEntity.ok(payrollService.updatePayroll(id, payrollUpdateDTO));
    }

    @Operation(summary = "Delete payroll")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted payroll"),
            @ApiResponse(responseCode = "404", description = "Payroll not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayroll(@PathVariable UUID id) {
        payrollService.deletePayroll(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== EMPLOYEE ENDPOINT ====================
    @Operation(summary = "Get current employee's own payrolls")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved payrolls")
    @GetMapping("/my-payrolls")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<PayrollResponseDTO>> getMyPayrolls(Authentication authentication) {
        UUID userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(payrollService.getPayrollsByUserId(userId));
    }

    // Helper method
    private UUID getCurrentUserId(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));
        return user.getId();
    }
}

