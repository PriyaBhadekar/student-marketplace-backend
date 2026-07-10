package com.marketplace.studentmarketplace.service;

import com.marketplace.studentmarketplace.dto.response.DashboardResponse;
import com.marketplace.studentmarketplace.entity.User;

public interface DashboardService {
    DashboardResponse getDashboard(User user);
}