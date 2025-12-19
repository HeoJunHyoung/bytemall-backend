package com.example.bytemallbackend.domain.catalog.category.dto.response;

import com.example.bytemallbackend.domain.catalog.category.entity.enumerate.RootCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class RootCategoryResponse {
    private String code; // "FASHION" (서버 전송용)
    private String name; // "패션의류" (화면 표시용)

    public static RootCategoryResponse fromEnum(RootCategory root) {
        return RootCategoryResponse.builder()
                .code(root.name())
                .name(root.getDescription())
                .build();
    }
}
