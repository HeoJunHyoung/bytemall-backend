package com.example.bytemallbackend.domain.catalog.category.dto.request;

import com.example.bytemallbackend.domain.catalog.category.entity.enumerate.RootCategory;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryCreateRequest {

    private String name; // 카테고리 이름 (하의, 상의, 양말, ...)

    private RootCategory rootCategory;

    private Long parentId; // 부모 카테고리의 ID (최상위 카테고리는 없을 수 있음)

}
