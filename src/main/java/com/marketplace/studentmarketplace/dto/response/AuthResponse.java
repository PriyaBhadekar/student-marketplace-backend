package com.marketplace.studentmarketplace.dto.response;

import lombok.*;

@Data @Builder
public class AuthResponse {
    private String token;
    private String email;
    private String fullName;
    private String message;
}