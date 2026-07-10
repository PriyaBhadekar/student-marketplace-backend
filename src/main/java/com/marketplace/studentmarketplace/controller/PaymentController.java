package com.marketplace.studentmarketplace.controller;

import com.marketplace.studentmarketplace.dto.response.PaymentResponse;
import com.marketplace.studentmarketplace.entity.User;
import com.marketplace.studentmarketplace.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * POST /api/payments/pay/{orderId}
     * Triggers the simulated online payment for an existing order.
     * Only works for ONLINE payment method orders.
     */
    @PostMapping("/pay/{orderId}")
    public ResponseEntity<PaymentResponse> processPayment(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(paymentService.processOnlinePayment(orderId, user));
    }

    /**
     * GET /api/payments/order/{orderId}
     * Fetches payment details for a given order.
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetails(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId, user));
    }
}