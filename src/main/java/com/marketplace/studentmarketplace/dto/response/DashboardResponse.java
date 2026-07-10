package com.marketplace.studentmarketplace.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data @Builder
public class DashboardResponse {
    private UserProfileResponse profile;
    private List<ProductResponse> listedProducts;
    private List<OrderResponse> purchasedOrders;
    private BigDecimal totalEarnings;
    private int totalProductsListed;
    private int totalOrdersPlaced;
}