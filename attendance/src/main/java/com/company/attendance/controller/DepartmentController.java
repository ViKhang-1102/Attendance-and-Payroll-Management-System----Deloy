
package com.company.attendance.controller;

import com.company.attendance.dto.department.DepartmentResponseDTO;
import com.company.attendance.model.entity.Department;
import com.company.attendance.repository.DepartmentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Department Management", description = "APIs for managing departments (Admin only)")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    @Data
    @AllArgsConstructor
    public static class DepartmentCreateUpdateDTO {
        private String name;
        private String description;
    }

    @Operation(summary = "Get all departments")
    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList());
    }

    @Operation(summary = "Get department by ID")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        return ResponseEntity.ok(toResponseDTO(department));
    }

    @Operation(summary = "Create new department")
    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentCreateUpdateDTO dto) {
        if (departmentRepository.existsByName(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department name already exists");
        }
        Department department = Department.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        department = departmentRepository.save(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(department));
    }

    @Operation(summary = "Update department")
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentCreateUpdateDTO dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        
        if (!department.getName().equals(dto.getName()) && departmentRepository.existsByName(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department name already exists");
        }
        
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        department = departmentRepository.save(department);
        return ResponseEntity.ok(toResponseDTO(department));
    }

    @Operation(summary = "Delete department")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
        departmentRepository.delete(department);
        return ResponseEntity.noContent().build();
    }

    private DepartmentResponseDTO toResponseDTO(Department department) {
        return DepartmentResponseDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
}
