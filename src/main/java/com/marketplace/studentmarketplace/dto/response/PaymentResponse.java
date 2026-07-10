package com.marketplace.studentmarketplace.dto.response;

import com.marketplace.studentmarketplace.enums.PaymentMethod;
import com.marketplace.studentmarketplace.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class PaymentResponse {
    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private String failureReason;
    private LocalDateTime createdAt;
    private String message;
}