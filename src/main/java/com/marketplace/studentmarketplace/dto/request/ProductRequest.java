package com.marketplace.studentmarketplace.dto.request;

import com.marketplace.studentmarketplace.enums.ProductType;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Product type is required")
    private ProductType productType;

    private String subject;
    private String semester;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity = 1;
}