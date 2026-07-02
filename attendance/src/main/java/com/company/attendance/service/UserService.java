
package com.company.attendance.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.company.attendance.dto.user.UserCreateDTO;
import com.company.attendance.dto.user.UserResponseDTO;
import com.company.attendance.dto.user.UserUpdateDTO;
import com.company.attendance.mapper.UserMapper;
import com.company.attendance.model.entity.Department;
import com.company.attendance.model.entity.Position;
import com.company.attendance.model.entity.User;
import com.company.attendance.model.enums.Role;
import com.company.attendance.repository.DepartmentRepository;
import com.company.attendance.repository.PositionRepository;
import com.company.attendance.repository.UserRepository;
import com.company.attendance.repository.WorkShiftRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final WorkShiftRepository workShiftRepository;

    // Lấy tất cả users
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponseDTO)
                .toList();
    }

    // Lấy user theo ID
    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
        return userMapper.toUserResponseDTO(user);
    }

    // Lấy tất cả active employees
    public List<UserResponseDTO> getActiveEmployees() {
        return userRepository.findByIsActiveTrueAndRoleNot(Role.ADMIN)
                .stream()
                .map(userMapper::toUserResponseDTO)
                .toList();
    }

    // Tạo user mới
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        if (userRepository.existsByEmail(userCreateDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        User user = userMapper.toUser(userCreateDTO);
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        
        // Set position if provided
        if (userCreateDTO.getPositionId() != null) {
            Position position = positionRepository.findById(userCreateDTO.getPositionId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
            user.setPosition(position);
        }
        
        // Set department if provided
        if (userCreateDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(userCreateDTO.getDepartmentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
            user.setDepartment(department);
        }

        // Set work shift if provided
        if (userCreateDTO.getWorkShiftId() != null) {
            com.company.attendance.model.entity.WorkShift workShift = workShiftRepository.findById(userCreateDTO.getWorkShiftId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work shift not found"));
            user.setWorkShift(workShift);
        }
        
        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }
        if (user.getLeaveBalance() == null) {
            user.setLeaveBalance(12);
        }
        user = userRepository.save(user);
        return userMapper.toUserResponseDTO(user);
    }

    // Cập nhật user
    public UserResponseDTO updateUser(UUID id, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
        if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(userUpdateDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        
        // Update position if provided
        if (userUpdateDTO.getPositionId() != null) {
            Position position = positionRepository.findById(userUpdateDTO.getPositionId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
            user.setPosition(position);
        }
        
        // Update department if provided
        if (userUpdateDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(userUpdateDTO.getDepartmentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
            user.setDepartment(department);
        }

        // Update work shift if provided
        if (userUpdateDTO.getWorkShiftId() != null) {
            com.company.attendance.model.entity.WorkShift workShift = workShiftRepository.findById(userUpdateDTO.getWorkShiftId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work shift not found"));
            user.setWorkShift(workShift);
        }
        
        userMapper.updateUserFromDTO(userUpdateDTO, user);
        if (userUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }
        user = userRepository.save(user);
        return userMapper.toUserResponseDTO(user);
    }

    // Xóa user (soft delete - set isActive to false)
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
        user.setIsActive(false);
        userRepository.save(user);
    }
}

