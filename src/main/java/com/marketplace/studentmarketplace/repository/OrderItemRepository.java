package com.marketplace.studentmarketplace.repository;

import com.marketplace.studentmarketplace.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}