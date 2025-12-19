package com.example.bytemallbackend.domain.catalog.category.dto.response;

import com.example.bytemallbackend.domain.catalog.category.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponse {
    private Long categoryId;
    private String name;
    private Integer depth;
    private Integer displayOrder;
    private Long parentId; // 최상위면 null

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .depth(category.getDepth())
                .displayOrder(category.getDisplayOrder())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .build();
    }
}