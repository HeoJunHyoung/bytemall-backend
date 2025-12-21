package com.example.bytemallbackend.domain.cart.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemDeleteRequest {

    private Long cartItemId;

}
