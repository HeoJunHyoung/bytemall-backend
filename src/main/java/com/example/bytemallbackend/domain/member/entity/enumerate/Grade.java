package com.example.bytemallbackend.domain.member.entity.enumerate;

import lombok.Getter;

@Getter
public enum Grade {
    IRON("아이언"),
    SILVER("실버"),
    GOLD("골드"),
    DIAMOND("다이아몬드");

    private String description;

    Grade(String description) {
        this.description = description;
    }

}
