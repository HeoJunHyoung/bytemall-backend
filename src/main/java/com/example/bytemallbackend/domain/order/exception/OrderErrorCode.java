package com.example.bytemallbackend.domain.order.exception;

import com.example.bytemallbackend.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OrderErrorCode implements ErrorCode {

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "O-001", "존재하지 않는 주문입니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "O-006", "해당 주문을 취소할 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    OrderErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
