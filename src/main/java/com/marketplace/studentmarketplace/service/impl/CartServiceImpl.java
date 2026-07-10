package com.marketplace.studentmarketplace.service.impl;

import com.marketplace.studentmarketplace.dto.request.CartRequest;
import com.marketplace.studentmarketplace.dto.response.CartResponse;
import com.marketplace.studentmarketplace.entity.*;
import com.marketplace.studentmarketplace.exception.*;
import com.marketplace.studentmarketplace.repository.*;
import com.marketplace.studentmarketplace.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    public CartResponse addToCart(User user, CartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (product.getSeller().getId().equals(user.getId())) {
            throw new BadRequestException("You cannot add your own product to cart");
        }

        // If already in cart, increase quantity
        cartItemRepository.findByUserAndProduct(user, product).ifPresentOrElse(
                existing -> {
                    existing.setQuantity(existing.getQuantity() + request.getQuantity());
                    cartItemRepository.save(existing);
                },
                () -> {
                    CartItem item = CartItem.builder()
                            .user(user).product(product)
                            .quantity(request.getQuantity())
                            .build();
                    cartItemRepository.save(item);
                }
        );

        return getCart(user);
    }

    @Override
    public CartResponse getCart(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);

        List<CartResponse.CartItemResponse> itemResponses = items.stream().map(item -> {
            BigDecimal subtotal = item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            return CartResponse.CartItemResponse.builder()
                    .cartItemId(item.getId())
                    .productId(item.getProduct().getId())
                    .productTitle(item.getProduct().getTitle())
                    .productPrice(item.getProduct().getPrice())
                    .quantity(item.getQuantity())
                    .subtotal(subtotal)
                    .build();
        }).toList();

        BigDecimal total = itemResponses.stream()
                .map(CartResponse.CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder().items(itemResponses).totalPrice(total).build();
    }

    @Override
    public CartResponse updateCartItem(User user, Long cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Unauthorized action");
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);
        return getCart(user);
    }

    @Override
    public void removeFromCart(User user, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Unauthorized action");
        }
        cartItemRepository.delete(item);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
}