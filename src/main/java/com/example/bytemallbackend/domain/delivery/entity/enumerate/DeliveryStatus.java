package com.example.bytemallbackend.domain.delivery.entity.enumerate;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    READY("배송 준비중"),
    SHIPPING("배송 중"),
    COMP("배송 완료"),
    CANCELED("배송 취소");

    private String description;

    DeliveryStatus(String description) {
        this.description = description;
    }
}