package com.marketplace.studentmarketplace.repository;

import com.marketplace.studentmarketplace.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyer(User buyer);
}