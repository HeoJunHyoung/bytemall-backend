package com.example.bytemallbackend.domain.catalog.product.dto.request;

import com.example.bytemallbackend.domain.catalog.product.entity.enumerate.ProductStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductUpdateRequest {

    private String name;
    private Integer price;
    private Integer stockQuantity;
    private String description;
    private ProductStatus status; // 판매중, 품절, 판매중지 상태 변경 가능
    private Long categoryId; // 카테고리 이동 시 사용
}
