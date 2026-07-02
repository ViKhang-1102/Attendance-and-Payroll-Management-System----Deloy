
package com.company.attendance.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.company.attendance.dto.AuthResponse;
import com.company.attendance.dto.LoginRequest;
import com.company.attendance.dto.RegisterRequest;
import com.company.attendance.model.entity.User;
import com.company.attendance.repository.UserRepository;
import com.company.attendance.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getIdentifier(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmailOrStaffId(loginRequest.getIdentifier(), loginRequest.getIdentifier())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String token = jwtService.generateToken((org.springframework.security.core.userdetails.User) authentication.getPrincipal());

        return buildAuthResponse(user, token);
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullName(registerRequest.getFullName())
                .role(registerRequest.getRole())
                .hourlyRate(registerRequest.getHourlyRate())
                .baseSalary(registerRequest.getBaseSalary())
                .leaveBalance(12) // Default 12 days
                .isActive(true)
                .build();

        user = userRepository.save(user);

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        java.util.Collections.singletonList(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                        "ROLE_" + user.getRole().name()
                                )
                        )
                )
        );

        return buildAuthResponse(user, token);
    }

    public AuthResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        String email = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return buildAuthResponse(user, null);
    }

    public AuthResponse updateProfile(com.company.attendance.dto.user.UserProfileUpdateDTO profileUpdateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        String email = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setPhone(profileUpdateDTO.getPhone());
        user.setAddress(profileUpdateDTO.getAddress());
        user.setDateOfBirth(profileUpdateDTO.getDateOfBirth());

        user = userRepository.save(user);

        return buildAuthResponse(user, null);
    }

    private AuthResponse buildAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .staffId(user.getStaffId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())
                .hourlyRate(user.getHourlyRate())
                .baseSalary(user.getBaseSalary())
                .positionName(user.getPosition() != null ? user.getPosition().getName() : null)
                .departmentName(user.getDepartment() != null ? user.getDepartment().getName() : null)
                .leaveBalance(user.getLeaveBalance())
                .avatarUrl(user.getAvatarUrl())
                .isActive(user.getIsActive())
                .build();
    }
}
