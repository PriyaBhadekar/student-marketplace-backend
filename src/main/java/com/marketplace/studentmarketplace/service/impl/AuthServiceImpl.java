package com.marketplace.studentmarketplace.service.impl;

import com.marketplace.studentmarketplace.dto.request.*;
import com.marketplace.studentmarketplace.dto.response.AuthResponse;
import com.marketplace.studentmarketplace.entity.User;
import com.marketplace.studentmarketplace.enums.Role;
import com.marketplace.studentmarketplace.exception.BadRequestException;
import com.marketplace.studentmarketplace.repository.UserRepository;
import com.marketplace.studentmarketplace.security.JwtUtil;
import com.marketplace.studentmarketplace.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .collegeName(request.getCollegeName())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.ROLE_STUDENT)
                .build();

        userRepository.save(user);
        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .message("Registration successful")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));

        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .message("Login successful")
                .build();
    }
}