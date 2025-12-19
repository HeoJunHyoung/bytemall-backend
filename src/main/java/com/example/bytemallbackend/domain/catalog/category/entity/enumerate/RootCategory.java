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

}
