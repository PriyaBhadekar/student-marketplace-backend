package com.marketplace.studentmarketplace.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder
public class UserProfileResponse {
    private Long id;
    private String fullName;
    private String email;
    private String collegeName;
    private String phoneNumber;
    private LocalDateTime createdAt;
}