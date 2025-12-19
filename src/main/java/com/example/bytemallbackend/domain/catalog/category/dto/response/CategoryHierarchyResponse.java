package com.example.bytemallbackend.domain.catalog.category.dto.response;

import com.example.bytemallbackend.domain.catalog.category.entity.Category;
import com.example.bytemallbackend.domain.catalog.category.entity.enumerate.RootCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CategoryHierarchyResponse {

    private Long id;
    private String name;
    private Integer depth;
    private Integer displayOrder;
    private List<CategoryHierarchyResponse> children = new ArrayList<>(); // 자식 카테고리 리스트 (무한 중첩)
    private RootCategory rootCategory; // 그룹핑 용도


    public static CategoryHierarchyResponse from(Category category) {
        return CategoryHierarchyResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .depth(category.getDepth())
                .displayOrder(category.getDisplayOrder())
                .rootCategory(category.getRootCategory())
                .build();
    }

    // 자식 추가 편의 메서드
    public void addChild(CategoryHierarchyResponse child) {
        this.children.add(child);
    }
}