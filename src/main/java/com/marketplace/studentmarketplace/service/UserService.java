package com.marketplace.studentmarketplace.service;

import com.marketplace.studentmarketplace.dto.request.UpdateProfileRequest;
import com.marketplace.studentmarketplace.dto.response.UserProfileResponse;
import com.marketplace.studentmarketplace.entity.User;

public interface UserService {
    UserProfileResponse getProfile(User user);
    UserProfileResponse updateProfile(User user, UpdateProfileRequest request);
}