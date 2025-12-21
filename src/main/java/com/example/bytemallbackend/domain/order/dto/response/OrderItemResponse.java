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
    private String optionName;
    private Integer price;
    private Integer count;
    private Integer totalPrice;

    private OrderItemResponse(Long orderItemId, String productName, String optionName, Integer price, Integer count, Integer totalPrice) {
        this.orderItemId = orderItemId;
        this.productName = productName;
        this.optionName = optionName;
        this.price = price;
        this.count = count;
        this.totalPrice = totalPrice;
    }

    public static OrderItemResponse fromEntity(OrderItem orderItem) {
        String optName = (orderItem.getProductOption() != null)
                ? orderItem.getProductOption().getOptionName()
                : "기본 옵션";

        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getProduct().getName(),
                optName,
                orderItem.getOrderPrice(),
                orderItem.getCount(),
                orderItem.getTotalPrice()
        );
    }
}