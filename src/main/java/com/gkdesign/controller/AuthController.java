package com.gkdesign.controller;

import com.gkdesign.dto.request.LoginRequest;
import com.gkdesign.dto.response.ApiResponse;
import com.gkdesign.dto.response.AuthResponse;
import com.gkdesign.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request), "Logged in successfully.");
    }
}
