package com.example.bytemallbackend.domain.catalog.category.entity.enumerate;

import lombok.Getter;

/**
 * 아래는 최상위 카테고리가 아님.
 * ㄴ 최상위 카테고리는 Category 테이블에서 결정되며, 이건 단순히 그룹 이름표(태그)의 용도임.
 */
@Getter
public enum RootCategory {

    FASHION("패션"),
    BEAUTY("뷰티"),
    FOOD("식품"),
    DAILY("생필품"),
    ELECTRONICS("가전/디지털"),
    FURNITURE("가구/인테리어"),
    BOOK("도서/문구");

    private final String description;

    RootCategory(String description) {
        this.description = description;
    }

    public static RootCategory findByName(String name) {
        for (RootCategory category : values()) {
            // 1. "패션" == "패션" (한글 매칭)
            if (category.getDescription().equals(name)) {
                return category;
            }
            // 2. "FASHION" == "FASHION" (영어 매칭 - 대소문자 무관하게 처리)
            if (category.name().equalsIgnoreCase(name)) {
                return category;
            }
        }
        // 못 찾으면 null 반환
        return null;
    }

}
