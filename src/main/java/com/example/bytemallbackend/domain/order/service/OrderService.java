package com.example.bytemallbackend.domain.order.service;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.exception.ProductErrorCode;
import com.example.bytemallbackend.domain.catalog.product.repository.ProductRepository;
import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.domain.member.exception.MemberErrorCode;
import com.example.bytemallbackend.domain.member.repository.MemberRepository;
import com.example.bytemallbackend.domain.order.dto.request.OrderItemRequest;
import com.example.bytemallbackend.domain.order.dto.request.OrderRequest;
import com.example.bytemallbackend.domain.order.dto.response.OrderDetailsResponse;
import com.example.bytemallbackend.domain.order.dto.response.OrderResponse;
import com.example.bytemallbackend.domain.order.entity.Order;
import com.example.bytemallbackend.domain.order.entity.OrderItem;
import com.example.bytemallbackend.domain.order.entity.enumerate.OrderStatus;
import com.example.bytemallbackend.domain.order.exception.OrderErrorCode;
import com.example.bytemallbackend.domain.order.repository.OrderRepository;
import com.example.bytemallbackend.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 주문 생성
    @Transactional
    public void createOrder(Long memberId, OrderRequest orderRequest) {

        // 1. 회원 조회
        Member customer = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 2-1. 주문 DTO에서 주문 상품 DTO 추출
        List<OrderItemRequest> orderItemRequests = orderRequest.getOrderItemRequests();
        // 2.2. 주문 상품 생성 (내부적으로 재고 차감 로직 실행)
        List<OrderItem> orderItems = orderItemRequests.stream()
                .map((orderItem) -> {
                    Product product = productRepository.findById(orderItem.getProductId())
                            .orElseThrow(() -> new BusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));
                    return OrderItem.createOrderItem(product, orderItem.getCount());
                })
                .toList();

        Order order = Order.createOrder(customer, OrderStatus.COMPLETED, orderItems);
        orderRepository.save(order);
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long memberId, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(OrderErrorCode.ORDER_NOT_FOUND));

        validateOrderOwner(memberId, order);

        order.cancel();

    }

    // 주문 목록 조회
    public Page<OrderResponse> getOrders(Long memberId, Pageable pageable) {
        return orderRepository.findAllByMemberId(memberId, pageable)
                .map(OrderResponse::fromEntity);
    }

    // 주문 상세 조회
    public OrderDetailsResponse getOrder(Long memberId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(OrderErrorCode.ORDER_NOT_FOUND));

        return OrderDetailsResponse.fromEntity(order);
    }


    // 헬퍼 메서드
    public void validateOrderOwner(Long memberId, Order order) {
        if (!order.getMember().getId().equals(memberId)) {
            throw new BusinessException(OrderErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

}
