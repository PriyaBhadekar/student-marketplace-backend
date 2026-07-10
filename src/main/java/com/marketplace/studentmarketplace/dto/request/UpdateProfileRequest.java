package com.marketplace.studentmarketplace.dto.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private String collegeName;
    private String phoneNumber;
}