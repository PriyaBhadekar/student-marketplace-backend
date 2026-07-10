package com.marketplace.studentmarketplace.service.impl;

import com.marketplace.studentmarketplace.dto.response.PaymentResponse;
import com.marketplace.studentmarketplace.entity.*;
import com.marketplace.studentmarketplace.enums.PaymentStatus;
import com.marketplace.studentmarketplace.exception.*;
import com.marketplace.studentmarketplace.repository.*;
import com.marketplace.studentmarketplace.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaymentResponse processOnlinePayment(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        // Ownership check
        if (!order.getBuyer().getId().equals(user.getId())) {
            throw new BadRequestException("Access denied");
        }

        // Prevent double-processing
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BadRequestException("This order has already been paid.");
        }

        // ── Simulated Payment Gateway ──────────────────────────────────────
        // Generate a transaction reference
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();

        // Simulate: 90% success rate (for demo; always succeeds unless you flip the flag)
        boolean paymentSucceeded = simulateGateway();
        // ──────────────────────────────────────────────────────────────────

        PaymentStatus resultStatus = paymentSucceeded ? PaymentStatus.PAID : PaymentStatus.FAILED;
        String failureReason = paymentSucceeded ? null : "Simulated gateway decline — insufficient funds";

        // Persist payment record
        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(resultStatus)
                .transactionId(paymentSucceeded ? transactionId : null)
                .failureReason(failureReason)
                .build();

        paymentRepository.save(payment);

        // Update order payment status and transaction reference
        order.setPaymentStatus(resultStatus);
        orderRepository.save(order);

        log.info("Payment {} for order {} — status: {}", transactionId, orderId, resultStatus);

        return buildResponse(payment, paymentSucceeded
                ? "Payment successful! Your order is confirmed."
                : "Payment failed. Please retry.");
    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getBuyer().getId().equals(user.getId())) {
            throw new BadRequestException("Access denied");
        }

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("No payment record found for this order"));

        return buildResponse(payment, null);
    }

    // ── Simulated Gateway: returns true 90% of the time ──
    private boolean simulateGateway() {
        return Math.random() > 0.1;
    }

    private PaymentResponse buildResponse(Payment payment, String message) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .failureReason(payment.getFailureReason())
                .createdAt(payment.getCreatedAt())
                .message(message)
                .build();
    }
}