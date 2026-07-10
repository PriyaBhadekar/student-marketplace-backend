package com.marketplace.studentmarketplace.service;

import com.marketplace.studentmarketplace.dto.request.*;
import com.marketplace.studentmarketplace.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}