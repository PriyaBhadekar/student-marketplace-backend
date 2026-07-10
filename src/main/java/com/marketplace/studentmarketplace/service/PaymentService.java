package com.marketplace.studentmarketplace.service;

import com.marketplace.studentmarketplace.dto.response.PaymentResponse;
import com.marketplace.studentmarketplace.entity.User;

public interface PaymentService {

    // Called internally after order creation for ONLINE payments
    PaymentResponse processOnlinePayment(Long orderId, User user);

    // Fetch payment details for an order
    PaymentResponse getPaymentByOrderId(Long orderId, User user);
}