package com.example.bytemallbackend.domain.member.entity.enumerate;

import lombok.Getter;

@Getter
public enum Role {
    CUSTOMER("구매자"),
    SELLER("판매자"),
    ADMIN("관리자");

    private final String description;

    Role(String description) {
        this.description = description;
    }
}
