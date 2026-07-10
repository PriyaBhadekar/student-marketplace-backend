package com.marketplace.studentmarketplace.dto.response;

import com.marketplace.studentmarketplace.enums.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class ProductResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private ProductType productType;
    private ProductStatus status;
    private String subject;
    private String semester;
    private String fileUrl;
    private String sellerName;
    private String sellerCollege;
    private Integer quantity;
    private LocalDateTime createdAt;
}