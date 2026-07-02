
package com.company.attendance.service;

import com.company.attendance.dto.workshift.WorkShiftCreateDTO;
import com.company.attendance.dto.workshift.WorkShiftResponseDTO;
import com.company.attendance.dto.workshift.WorkShiftUpdateDTO;
import com.company.attendance.mapper.WorkShiftMapper;
import com.company.attendance.model.entity.WorkShift;
import com.company.attendance.repository.WorkShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkShiftService {
    private final WorkShiftRepository workShiftRepository;
    private final WorkShiftMapper workShiftMapper;

    public List<WorkShiftResponseDTO> getAllWorkShifts() {
        return workShiftRepository.findAll()
                .stream()
                .map(workShiftMapper::toWorkShiftResponseDTO)
                .toList();
    }

    public WorkShiftResponseDTO getWorkShiftById(UUID id) {
        WorkShift workShift = workShiftRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work Shift not found"));
        return workShiftMapper.toWorkShiftResponseDTO(workShift);
    }

    public WorkShiftResponseDTO createWorkShift(WorkShiftCreateDTO workShiftCreateDTO) {
        WorkShift workShift = workShiftMapper.toWorkShift(workShiftCreateDTO);
        workShift = workShiftRepository.save(workShift);
        return workShiftMapper.toWorkShiftResponseDTO(workShift);
    }

    public WorkShiftResponseDTO updateWorkShift(UUID id, WorkShiftUpdateDTO workShiftUpdateDTO) {
        WorkShift workShift = workShiftRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work Shift not found"));
        workShiftMapper.updateWorkShiftFromDTO(workShiftUpdateDTO, workShift);
        workShift = workShiftRepository.save(workShift);
        return workShiftMapper.toWorkShiftResponseDTO(workShift);
    }

    public void deleteWorkShift(UUID id) {
        if (!workShiftRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Work Shift not found");
        }
        workShiftRepository.deleteById(id);
    }
}
