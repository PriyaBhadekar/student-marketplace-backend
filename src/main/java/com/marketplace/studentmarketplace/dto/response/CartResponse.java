package com.marketplace.studentmarketplace.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data @Builder
public class CartResponse {
    private List<CartItemResponse> items;
    private BigDecimal totalPrice;

    @Data @Builder
    public static class CartItemResponse {
        private Long cartItemId;
        private Long productId;
        private String productTitle;
        private BigDecimal productPrice;
        private Integer quantity;
        private BigDecimal subtotal;
    }
}
