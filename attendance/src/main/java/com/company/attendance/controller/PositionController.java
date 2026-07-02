
package com.company.attendance.controller;

import com.company.attendance.dto.position.PositionResponseDTO;
import com.company.attendance.model.entity.Position;
import com.company.attendance.repository.PositionRepository;
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
@RequestMapping("/api/positions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Position Management", description = "APIs for managing positions (Admin only)")
public class PositionController {

    private final PositionRepository positionRepository;

    @Data
    @AllArgsConstructor
    public static class PositionCreateUpdateDTO {
        private String name;
        private String description;
    }

    @Operation(summary = "Get all positions")
    @GetMapping
    public ResponseEntity<List<PositionResponseDTO>> getAllPositions() {
        return ResponseEntity.ok(positionRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList());
    }

    @Operation(summary = "Get position by ID")
    @GetMapping("/{id}")
    public ResponseEntity<PositionResponseDTO> getPositionById(@PathVariable UUID id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
        return ResponseEntity.ok(toResponseDTO(position));
    }

    @Operation(summary = "Create new position")
    @PostMapping
    public ResponseEntity<PositionResponseDTO> createPosition(@Valid @RequestBody PositionCreateUpdateDTO dto) {
        if (positionRepository.existsByName(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Position name already exists");
        }
        Position position = Position.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        position = positionRepository.save(position);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(position));
    }

    @Operation(summary = "Update position")
    @PutMapping("/{id}")
    public ResponseEntity<PositionResponseDTO> updatePosition(
            @PathVariable UUID id,
            @Valid @RequestBody PositionCreateUpdateDTO dto) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
        
        if (!position.getName().equals(dto.getName()) && positionRepository.existsByName(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Position name already exists");
        }
        
        position.setName(dto.getName());
        position.setDescription(dto.getDescription());
        position = positionRepository.save(position);
        return ResponseEntity.ok(toResponseDTO(position));
    }

    @Operation(summary = "Delete position")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable UUID id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
        positionRepository.delete(position);
        return ResponseEntity.noContent().build();
    }

    private PositionResponseDTO toResponseDTO(Position position) {
        return PositionResponseDTO.builder()
                .id(position.getId())
                .name(position.getName())
                .description(position.getDescription())
                .createdAt(position.getCreatedAt())
                .updatedAt(position.getUpdatedAt())
                .build();
    }
}
