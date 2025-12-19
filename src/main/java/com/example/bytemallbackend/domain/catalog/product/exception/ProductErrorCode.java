package com.example.bytemallbackend.domain.catalog.product.exception;

import com.example.bytemallbackend.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProductErrorCode implements ErrorCode {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "C-001", "존재하지 않는 상품입니다."),
    PRODUCT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "C-002", "재고가 부족합니다. 재고를 확인해주세요.");



    private final HttpStatus status;
    private final String code;
    private final String message;

    ProductErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
