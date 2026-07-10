package com.marketplace.studentmarketplace.dto.response;

import com.marketplace.studentmarketplace.enums.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder
public class OrderResponse {
    private Long orderId;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;       // ✅ NEW
    private String deliveryAddress;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;

    @Data @Builder
    public static class OrderItemResponse {
        private Long productId;
        private String productTitle;
        private Integer quantity;
        private BigDecimal priceAtPurchase;
    }
}