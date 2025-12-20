package com.example.bytemallbackend.domain.order.controller;

import com.example.bytemallbackend.domain.order.dto.request.OrderItemRequest;
import com.example.bytemallbackend.domain.order.dto.request.OrderRequest;
import com.example.bytemallbackend.domain.order.dto.response.OrderDetailsResponse;
import com.example.bytemallbackend.domain.order.dto.response.OrderResponse;
import com.example.bytemallbackend.domain.order.service.OrderService;
import com.example.bytemallbackend.global.security.principal.AuthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<Void> createOrder(@AuthenticationPrincipal AuthMember authMember, @RequestBody OrderRequest request) {
        orderService.createOrder(authMember.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 주문 취소
    @PostMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@AuthenticationPrincipal AuthMember authMember, @PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(authMember.getId(), orderId);
        return ResponseEntity.ok().build();
    }

    // 주문 목록 조회
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrders(@AuthenticationPrincipal AuthMember authMember,
                                                         @PageableDefault(size = 10, sort = "orderDate", direction = Sort.Direction.DESC)Pageable pageable) {
        Page<OrderResponse> orderResponses = orderService.getOrders(authMember.getId(), pageable);
        return ResponseEntity.ok(orderResponses);
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsResponse> getOrder(@AuthenticationPrincipal AuthMember authMember,
                                                         @PathVariable("orderId") Long orderId) {
        OrderDetailsResponse orderDetailsResponse = orderService.getOrder(authMember.getId(), orderId);
        return ResponseEntity.ok(orderDetailsResponse);
    }


}
