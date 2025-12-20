package com.example.bytemallbackend.domain.order.dto.response;

import com.example.bytemallbackend.domain.order.entity.Order;
import com.example.bytemallbackend.domain.order.entity.enumerate.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderResponse {

    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Integer totalPrice;
    private String representativeProductName;

    private OrderResponse(Long orderId, LocalDateTime orderDate, OrderStatus orderStatus, Integer totalPrice, String representativeProductName) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.representativeProductName = representativeProductName;
    }

    public static OrderResponse fromEntity(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalPrice(),
                createRepresentativeProductName(order)
        );
    }

    private static String createRepresentativeProductName(Order order) {
        int itemCount = order.getOrderItems().size();
        String firstProductName = order.getOrderItems().get(0).getProduct().getName();

        if (itemCount == 1) {
            return firstProductName;
        }
        return firstProductName + " 외 " + (itemCount - 1) + "건";
    }
}

