package com.example.bytemallbackend.domain.cart.dto.response;

import com.example.bytemallbackend.domain.cart.entity.CartItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemResponse {

    private Long cartItemId;
    private Long productId;
    private String productName;
    private Integer price;
    private Integer count;
    private Integer totalPrice;

    private CartItemResponse(Long cartItemId, Long productId, String productName, Integer price, Integer count, Integer totalPrice) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.count = count;
        this.totalPrice = totalPrice;
    }

    public static CartItemResponse fromEntity(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getProduct().getPrice(),
                cartItem.getCount(),
                cartItem.getProduct().getPrice() * cartItem.getCount()
        );
    }
}