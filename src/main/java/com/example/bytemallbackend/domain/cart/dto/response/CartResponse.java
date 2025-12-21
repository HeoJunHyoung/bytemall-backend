package com.example.bytemallbackend.domain.cart.dto.response;

import com.example.bytemallbackend.domain.cart.entity.Cart;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartResponse {

    private List<CartItemResponse> cartItems;
    private Integer totalOrderPrice;

    private CartResponse(List<CartItemResponse> cartItems, Integer totalOrderPrice) {
        this.cartItems = cartItems;
        this.totalOrderPrice = totalOrderPrice;
    }

    public static CartResponse fromEntity(Cart cart) {
        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(CartItemResponse::fromEntity)
                .collect(Collectors.toList());

        int total = items.stream()
                .mapToInt(CartItemResponse::getTotalPrice)
                .sum();

        return new CartResponse(items, total);
    }
}