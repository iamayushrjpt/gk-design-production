package com.gkdesign.service;

import com.gkdesign.dto.request.LoginRequest;
import com.gkdesign.dto.response.AuthResponse;
import com.gkdesign.exception.InvalidCredentialsException;
import com.gkdesign.model.AdminUser;
import com.gkdesign.repository.AdminUserRepository;
import com.gkdesign.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        AdminUser user = adminUserRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

        if (!user.isActive()) {
            throw new InvalidCredentialsException("This admin account has been deactivated.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        user.setLastLoginAt(Instant.now());
        adminUserRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}
