package com.example.bytemallbackend.domain.catalog.product.entity.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum ProductStatus {

    ON_SALE("판매중"),
    SOLD_OUT("품절"),
    STOPPED("판매중지");

    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }
}