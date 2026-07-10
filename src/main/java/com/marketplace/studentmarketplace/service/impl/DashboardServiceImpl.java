package com.marketplace.studentmarketplace.service.impl;

import com.marketplace.studentmarketplace.dto.response.DashboardResponse;
import com.marketplace.studentmarketplace.entity.User;
import com.marketplace.studentmarketplace.repository.ProductRepository;
import com.marketplace.studentmarketplace.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final ProductRepository productRepository;

    @Override
    public DashboardResponse getDashboard(User user) {
        var profile = userService.getProfile(user);
        var listedProducts = productService.getMyProducts(user);
        var purchasedOrders = orderService.getMyOrders(user);
        var totalEarnings = productRepository.calculateTotalEarnings(user.getId());

        return DashboardResponse.builder()
                .profile(profile)
                .listedProducts(listedProducts)
                .purchasedOrders(purchasedOrders)
                .totalEarnings(totalEarnings)
                .totalProductsListed(listedProducts.size())
                .totalOrdersPlaced(purchasedOrders.size())
                .build();
    }
}