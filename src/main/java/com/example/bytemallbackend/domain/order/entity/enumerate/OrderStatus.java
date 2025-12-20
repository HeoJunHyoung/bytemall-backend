package com.example.bytemallbackend.domain.order.entity.enumerate;

import lombok.Getter;

@Getter
public enum OrderStatus {

    COMPLETED("주문 완료"),
    CANCEL("주문 취소");

    private String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
