package com.example.bytemallbackend.domain.cart.controller;

import com.example.bytemallbackend.domain.cart.dto.request.CartItemAddRequest;
import com.example.bytemallbackend.domain.cart.dto.request.CartItemDeleteRequest;
import com.example.bytemallbackend.domain.cart.dto.request.CartItemUpdateRequest;
import com.example.bytemallbackend.domain.cart.dto.response.CartResponse;
import com.example.bytemallbackend.domain.cart.service.CartService;
import com.example.bytemallbackend.global.security.principal.AuthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    // 장바구니 상품 담기
    @PostMapping
    public ResponseEntity<Void> addCart(@AuthenticationPrincipal AuthMember authMember, @RequestBody CartItemAddRequest request) {
        cartService.addCart(authMember.getId(), request);
        return ResponseEntity.ok().build();
    }

    // 장바구니 상품 수량 변경
    @PatchMapping("items/{cartItemId}")
    public ResponseEntity<Void> updateCartItem(@AuthenticationPrincipal AuthMember authMember, @RequestBody CartItemUpdateRequest request) {
        cartService.updateCartItemCount(authMember.getId(), request);
        return ResponseEntity.ok().build();
    }

    // 장바구니 상품 제거
    @DeleteMapping("/items/")
    public ResponseEntity<Void> deleteCartItems(@AuthenticationPrincipal AuthMember authMember, @RequestBody List<CartItemDeleteRequest> requests) {
        cartService.deleteCartItems(authMember.getId(), requests);
        return ResponseEntity.ok().build();
    }

    // 장바구니 상품 비우기


    // 장바구니 상품 조회
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal AuthMember authMember) {
        CartResponse response = cartService.getCart(authMember.getId());
        return ResponseEntity.ok(response);
    }
}
