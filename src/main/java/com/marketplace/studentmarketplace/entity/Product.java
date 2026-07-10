package com.marketplace.studentmarketplace.entity;

import com.marketplace.studentmarketplace.enums.ProductStatus;
import com.marketplace.studentmarketplace.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;   // PHYSICAL or DIGITAL

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private String subject;       // e.g. "Mathematics", "Physics"
    private String semester;      // e.g. "Semester 3"
    private String filePath;      // Only for DIGITAL products (PDF path)
    private Integer quantity;     // number of units available

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = ProductStatus.AVAILABLE;
        if (quantity == null) quantity = 1;
    }
}