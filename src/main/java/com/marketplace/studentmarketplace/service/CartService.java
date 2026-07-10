package com.marketplace.studentmarketplace.service;

import com.marketplace.studentmarketplace.dto.request.CartRequest;
import com.marketplace.studentmarketplace.dto.response.CartResponse;
import com.marketplace.studentmarketplace.entity.User;

public interface CartService {
    CartResponse addToCart(User user, CartRequest request);
    CartResponse getCart(User user);
    CartResponse updateCartItem(User user, Long cartItemId, Integer quantity);
    void removeFromCart(User user, Long cartItemId);
    void clearCart(User user);
}