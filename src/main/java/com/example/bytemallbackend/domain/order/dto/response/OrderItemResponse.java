package com.example.bytemallbackend.domain.order.dto.response;

import com.example.bytemallbackend.domain.order.entity.OrderItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemResponse {

    private Long orderItemId;
    private String productName;
    private Integer price;
    private Integer count;
    private Integer totalPrice;

    private OrderItemResponse(Long orderItemId, String productName, Integer price, Integer count, Integer totalPrice) {
        this.orderItemId = orderItemId;
        this.productName = productName;
        this.price = price;
        this.count = count;
        this.totalPrice = totalPrice;
    }

    public static OrderItemResponse fromEntity(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProduct().getName(),
                orderItem.getPrice(),
                orderItem.getCount(),
                orderItem.getTotalPrice()
        );
    }

}
