package com.marketplace.studentmarketplace.service.impl;

import com.marketplace.studentmarketplace.dto.request.UpdateProfileRequest;
import com.marketplace.studentmarketplace.dto.response.UserProfileResponse;
import com.marketplace.studentmarketplace.entity.User;
import com.marketplace.studentmarketplace.repository.UserRepository;
import com.marketplace.studentmarketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserProfileResponse getProfile(User user) {
        return mapToResponse(user);
    }

    @Override
    public UserProfileResponse updateProfile(User user, UpdateProfileRequest request) {
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getCollegeName() != null) user.setCollegeName(request.getCollegeName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
        return mapToResponse(user);
    }

    private UserProfileResponse mapToResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .collegeName(user.getCollegeName())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .build();
    }
}