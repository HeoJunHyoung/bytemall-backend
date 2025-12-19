package com.example.bytemallbackend.domain.catalog.category.exception;

import com.example.bytemallbackend.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CategoryErrorCode implements ErrorCode {

    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "C-001", "존재하지 않는 카테고리입니다."),
    CANNOT_DELETE_HAS_PRODUCTS(HttpStatus.CONFLICT, "C-002", "상품이 존재하므로 카테고리를 삭제할 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    CategoryErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
