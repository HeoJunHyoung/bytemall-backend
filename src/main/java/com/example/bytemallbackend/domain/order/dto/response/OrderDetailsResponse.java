package com.example.bytemallbackend.domain.order.dto.response;

import com.example.bytemallbackend.domain.order.entity.Order;
import com.example.bytemallbackend.domain.order.entity.enumerate.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetailsResponse {

    private Long orderId;
    private OrderStatus orderStatus;
    private Integer totalPrice;
    private List<OrderItemResponse> orderItemResponses = new ArrayList<>();

    private OrderDetailsResponse(Long orderId, OrderStatus orderStatus, Integer totalPrice, List<OrderItemResponse> orderItemResponses) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.orderItemResponses = orderItemResponses;
    }

    public static OrderDetailsResponse fromEntity(Order order) {

        List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(OrderItemResponse::fromEntity)
                .collect(Collectors.toList());

        return new OrderDetailsResponse(order.getId(), order.getStatus(), order.getTotalPrice(), orderItemResponses);
    }
}
