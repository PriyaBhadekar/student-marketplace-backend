package com.marketplace.studentmarketplace.service.impl;

import com.marketplace.studentmarketplace.dto.request.OrderRequest;
import com.marketplace.studentmarketplace.dto.response.OrderResponse;
import com.marketplace.studentmarketplace.entity.*;
import com.marketplace.studentmarketplace.enums.*;
import com.marketplace.studentmarketplace.exception.*;
import com.marketplace.studentmarketplace.repository.*;
import com.marketplace.studentmarketplace.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;   // ✅ NEW injection

    @Override
    @Transactional
    public OrderResponse placeOrder(User buyer, OrderRequest request) {

        List<CartItem> cartItems = cartItemRepository.findByUser(buyer);
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Your cart is empty");
        }

        // ✅ SAFE STOCK VALIDATION
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            Integer quantity = product.getQuantity();

            if (product.getStatus() == ProductStatus.SOLD_OUT) {
                throw new BadRequestException("'" + product.getTitle() + "' is no longer available.");
            }

            if (quantity == null || quantity < cartItem.getQuantity()) {
                throw new BadRequestException(
                        "Not enough stock for '" + product.getTitle() +
                                "'. Available: " + (quantity == null ? 0 : quantity)
                );
            }
        }

        BigDecimal total = cartItems.stream()
                .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PaymentStatus initialPaymentStatus = (request.getPaymentMethod() == PaymentMethod.ONLINE)
                ? PaymentStatus.PAID
                : PaymentStatus.PENDING;

        Order order = Order.builder()
                .buyer(buyer)
                .totalAmount(total)
                .paymentMethod(request.getPaymentMethod())
                .deliveryAddress(request.getDeliveryAddress())
                .paymentStatus(initialPaymentStatus)
                .build();

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            Integer quantity = product.getQuantity() == null ? 0 : product.getQuantity();

            int newQty = quantity - cartItem.getQuantity();
            product.setQuantity(Math.max(newQty, 0));

            if (newQty <= 0) {
                product.setStatus(ProductStatus.SOLD_OUT);
            }

            productRepository.save(product);

            orderItems.add(OrderItem.builder()
                    .order(savedOrder)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build());
        }

        orderItemRepository.saveAll(orderItems);
        cartItemRepository.deleteByUser(buyer);

        savedOrder.setOrderItems(orderItems);
        return mapToResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getMyOrders(User buyer) {

        List<Order> orders = orderRepository.findByBuyer(buyer);

        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }


    private OrderResponse mapToResponse(Order order) {

        List<OrderResponse.OrderItemResponse> items =
                order.getOrderItems() == null
                        ? List.of()
                        : order.getOrderItems().stream().map(oi ->
                        OrderResponse.OrderItemResponse.builder()
                                .productId(oi.getProduct().getId())
                                .productTitle(oi.getProduct().getTitle())
                                .quantity(oi.getQuantity())
                                .priceAtPurchase(oi.getPriceAtPurchase())
                                .build()
                ).toList();

        String transactionId = null;
        if (order.getPayment() != null) {
            transactionId = order.getPayment().getTransactionId();
        }

        return OrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getOrderStatus())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .transactionId(transactionId)
                .deliveryAddress(order.getDeliveryAddress())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }

    @Override
    public OrderResponse getOrderById(User buyer, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getBuyer().getId().equals(buyer.getId())) {
            throw new BadRequestException("Access denied");
        }

        return mapToResponse(order);
    }
}