package com.example.bytemallbackend.domain.cart.exception;

import com.example.bytemallbackend.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CartErrorCode implements ErrorCode {

    // 장바구니 조회
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "CT-001", "존재하지 않는 장바구니입니다."),

    // 장바구니 아이템 조회
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "CT-002", "장바구니에서 해당 상품을 찾을 수 없습니다."),

    // 수량 및 입력값 검증
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "CT-003", "장바구니 수량은 1개 이상이어야 합니다."),

    // 권한 검증
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "CT-004", "해당 장바구니에 대한 접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    CartErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}