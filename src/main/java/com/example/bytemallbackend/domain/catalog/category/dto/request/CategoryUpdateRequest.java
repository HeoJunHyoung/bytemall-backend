package com.example.bytemallbackend.domain.catalog.category.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryUpdateRequest {

    private Long categoryId;          // 어떤 카테고리인지 식별

    private String name;      // 변경된 이름 (변경 안 됐으면 기존 이름 그대로 옴)

    private Integer displayOrder; // 최종 확정된 순서
}