package com.example.bytemallbackend.domain.catalog.product.dto.response;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.entity.enumerate.ProductStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSellerDetailsResponse {

    private Long id;
    private String name;
    private Integer price;
    private Integer stockQuantity;
    private String description;
    private ProductStatus status;

    // 카테고리 정보 포함
    private Long categoryId;
    private String categoryName;
    private String categoryPath;

    public static ProductSellerDetailsResponse fromEntity(Product entity) {
        return ProductSellerDetailsResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .stockQuantity(entity.getStockQuantity())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .categoryId(entity.getCategory().getId())
                .categoryName(entity.getCategory().getName())
                .categoryPath(entity.getCategory().getPath())
                .build();
    }

}
