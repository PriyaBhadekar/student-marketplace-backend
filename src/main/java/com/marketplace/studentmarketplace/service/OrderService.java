package com.marketplace.studentmarketplace.service;

import com.marketplace.studentmarketplace.dto.request.OrderRequest;
import com.marketplace.studentmarketplace.dto.response.OrderResponse;
import com.marketplace.studentmarketplace.entity.User;
import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(User buyer, OrderRequest request);
    List<OrderResponse> getMyOrders(User buyer);
    OrderResponse getOrderById(User buyer, Long orderId);
}