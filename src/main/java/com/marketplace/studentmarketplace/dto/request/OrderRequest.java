package com.marketplace.studentmarketplace.dto.request;

import com.marketplace.studentmarketplace.enums.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderRequest {
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String deliveryAddress;
}