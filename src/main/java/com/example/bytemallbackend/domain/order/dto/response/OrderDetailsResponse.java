package com.example.bytemallbackend.domain.order.dto.response;

import com.example.bytemallbackend.global.common.Address;
import com.example.bytemallbackend.domain.delivery.entity.enumerate.DeliveryStatus;
import com.example.bytemallbackend.domain.order.entity.Order;
import com.example.bytemallbackend.domain.order.entity.enumerate.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetailsResponse {

    private Long orderId;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    private Integer totalPrice;
    private LocalDateTime orderDate;
    private Address shippingAddress;
    private List<OrderItemResponse> orderItemResponses = new ArrayList<>();

    private OrderDetailsResponse(Long orderId, OrderStatus orderStatus, DeliveryStatus deliveryStatus,
                                 Integer totalPrice, LocalDateTime orderDate, Address shippingAddress,
                                 List<OrderItemResponse> orderItemResponses) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.shippingAddress = shippingAddress;
        this.orderItemResponses = orderItemResponses;
    }

    public static OrderDetailsResponse fromEntity(Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(OrderItemResponse::fromEntity)
                .collect(Collectors.toList());

        Address addr = order.getDelivery() != null ? order.getDelivery().getAddress() : null;
        DeliveryStatus ds = order.getDelivery() != null ? order.getDelivery().getStatus() : null;

        return new OrderDetailsResponse(
                order.getId(),
                order.getStatus(),
                ds,
                order.getTotalPrice(),
                order.getOrderDate(),
                addr,
                orderItemResponses
        );
    }
}
