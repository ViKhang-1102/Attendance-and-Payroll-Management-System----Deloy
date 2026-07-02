
package com.company.attendance.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.attendance.dto.AuthResponse;
import com.company.attendance.dto.LoginRequest;
import com.company.attendance.dto.RegisterRequest;
import com.company.attendance.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "User login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Register new user (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully registered user"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Not authorized")
    })
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse authResponse = authService.register(registerRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get current user information")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user info")
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<AuthResponse> getCurrentUser() {
        AuthResponse authResponse = authService.getCurrentUser();
        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Update current user profile")
    @ApiResponse(responseCode = "200", description = "Successfully updated profile")
    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<AuthResponse> updateProfile(@Valid @RequestBody com.company.attendance.dto.user.UserProfileUpdateDTO profileUpdateDTO) {
        AuthResponse authResponse = authService.updateProfile(profileUpdateDTO);
        return ResponseEntity.ok(authResponse);
    }
}

