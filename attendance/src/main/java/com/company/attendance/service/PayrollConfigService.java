
package com.company.attendance.service;

import com.company.attendance.dto.payrollconfig.PayrollConfigCreateDTO;
import com.company.attendance.dto.payrollconfig.PayrollConfigResponseDTO;
import com.company.attendance.dto.payrollconfig.PayrollConfigUpdateDTO;
import com.company.attendance.mapper.PayrollConfigMapper;
import com.company.attendance.model.entity.PayrollConfig;
import com.company.attendance.repository.PayrollConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayrollConfigService {
    private final PayrollConfigRepository payrollConfigRepository;
    private final PayrollConfigMapper payrollConfigMapper;

    public List<PayrollConfigResponseDTO> getAllPayrollConfigs() {
        return payrollConfigRepository.findAll()
                .stream()
                .map(payrollConfigMapper::toResponseDTO)
                .toList();
    }

    public PayrollConfigResponseDTO getActivePayrollConfig() {
        PayrollConfig config = payrollConfigRepository.findByIsActiveTrue()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active payroll config found"));
        return payrollConfigMapper.toResponseDTO(config);
    }

    public PayrollConfigResponseDTO createPayrollConfig(PayrollConfigCreateDTO dto) {
        payrollConfigRepository.findAll().forEach(c -> {
            c.setIsActive(false);
            payrollConfigRepository.save(c);
        });

        PayrollConfig config = payrollConfigMapper.toEntity(dto);
        config.setIsActive(true);
        config = payrollConfigRepository.save(config);
        return payrollConfigMapper.toResponseDTO(config);
    }

    public PayrollConfigResponseDTO updatePayrollConfig(UUID id, PayrollConfigUpdateDTO dto) {
        PayrollConfig config = payrollConfigRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payroll config not found"));
        payrollConfigMapper.updateEntityFromDTO(dto, config);
        config = payrollConfigRepository.save(config);
        return payrollConfigMapper.toResponseDTO(config);
    }

    public PayrollConfigResponseDTO setActive(UUID id) {
        payrollConfigRepository.findAll().forEach(c -> {
            c.setIsActive(c.getId().equals(id));
            payrollConfigRepository.save(c);
        });
        return getActivePayrollConfig();
    }
}
