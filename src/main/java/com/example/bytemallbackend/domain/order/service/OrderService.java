package com.example.bytemallbackend.domain.order.service;

import com.example.bytemallbackend.domain.cart.service.CartService;
import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.exception.ProductErrorCode;
import com.example.bytemallbackend.domain.catalog.product.repository.ProductRepository;
import com.example.bytemallbackend.global.common.Address;
import com.example.bytemallbackend.domain.delivery.entity.Delivery;
import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.domain.member.entity.MemberAddress;
import com.example.bytemallbackend.domain.member.exception.MemberErrorCode;
import com.example.bytemallbackend.domain.member.repository.MemberAddressRepository;
import com.example.bytemallbackend.domain.member.repository.MemberRepository;
import com.example.bytemallbackend.domain.order.dto.request.OrderItemRequest;
import com.example.bytemallbackend.domain.order.dto.request.OrderRequest;
import com.example.bytemallbackend.domain.order.dto.response.OrderDetailsResponse;
import com.example.bytemallbackend.domain.order.dto.response.OrderResponse;
import com.example.bytemallbackend.domain.order.entity.Order;
import com.example.bytemallbackend.domain.order.entity.OrderItem;
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

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final MemberAddressRepository memberAddressRepository;
    private final CartService cartService;

    // 주문 생성
    @Transactional
    public void createOrder(Long memberId, OrderRequest request) {

        // 1. 회원 조회
        Member customer = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 2-1. 배송지 설정 (주소록 ID 사용 or 직접 입력)
        Address address;
        if (request.getDeliveryAddressId() != null) {
            // 저장된 배송지 사용
            MemberAddress ma = memberAddressRepository.findById(request.getDeliveryAddressId())
                    .orElseThrow(() -> new BusinessException(MemberErrorCode.ADDRESS_NOT_FOUND));

            // 내 주소가 맞는지 검증
            if (!ma.getMember().getId().equals(memberId)) {
                throw new BusinessException(MemberErrorCode.ADDRESS_NOT_FOUND);
            }

            address = ma.getAddress();
        } else {
            // 신규 입력 주소 사용
            address = new Address(request.getZipcode(), request.getRoadAddress(), request.getDetailAddress());
        }
        // 2-2. 배송 생성
        Delivery delivery = Delivery.createDelivery(address);

        // 3-1. 주문 DTO에서 주문 상품 DTO 추출
        List<OrderItemRequest> orderItemRequests = request.getOrderItemRequests();
        // 3.2. 주문 상품 생성 (내부적으로 재고 차감 로직 실행)
        List<OrderItem> orderItems = orderItemRequests.stream()
                .map((orderItem) -> {
                    Product product = productRepository.findById(orderItem.getProductId())
                            .orElseThrow(() -> new BusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));
                    return OrderItem.createOrderItem(product, orderItem.getCount());
                })
                .toList();

        Order order = Order.createOrder(customer, delivery, orderItems);
        orderRepository.save(order);

        if (request.isFromCart()) {
            // 주문한 상품 ID 목록 추출
            List<Long> orderedProductIds = request.getOrderItemRequests().stream()
                    .map(OrderItemRequest::getProductId)
                    .toList();

            // CartService에 해당 상품 삭제 요청
            cartService.removeCartItemsByProductIds(memberId, orderedProductIds);
        }

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

        validateOrderOwner(memberId, order);

        return OrderDetailsResponse.fromEntity(order);
    }


    // 헬퍼 메서드
    public void validateOrderOwner(Long memberId, Order order) {
        if (!order.getMember().getId().equals(memberId)) {
            throw new BusinessException(OrderErrorCode.UNAUTHORIZED_ACCESS);
        }
    }

}
