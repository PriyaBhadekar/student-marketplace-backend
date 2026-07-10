package com.marketplace.studentmarketplace.repository;

import com.marketplace.studentmarketplace.entity.*;
import com.marketplace.studentmarketplace.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findBySeller(User seller);

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByTitleContainingIgnoreCaseOrSubjectContainingIgnoreCase(
            String title, String subject);

    @Query("SELECT COALESCE(SUM(oi.priceAtPurchase * oi.quantity), 0) " +
            "FROM OrderItem oi WHERE oi.product.seller.id = :sellerId")
    BigDecimal calculateTotalEarnings(Long sellerId);
}