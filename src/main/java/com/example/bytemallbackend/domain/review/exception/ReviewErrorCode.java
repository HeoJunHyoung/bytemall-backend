package com.example.bytemallbackend.domain.review.exception;

import com.example.bytemallbackend.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "R-001", "해당 리뷰를 찾을 수 없습니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "R-002", "이미 해당 주문 상품에 대한 리뷰가 존재합니다."),
    REVIEW_NOT_DELIVERED(HttpStatus.BAD_REQUEST, "R-003", "배송 완료된 상품만 리뷰를 작성할 수 있습니다."),
    REVIEW_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "R-004", "해당 주문에 대한 리뷰 작성 권한이 없습니다."),
    ORDER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "R-005", "주문 상품 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return this.status;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
