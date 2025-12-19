package com.example.bytemallbackend.domain.catalog.product.dto.response;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.entity.enumerate.ProductStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCustomerResponse {

    private Long id;
    private String name;
    private Integer price;
    private Integer stockQuantity;
    private ProductStatus status;

    public static ProductCustomerResponse fromEntity(Product entity) {
        return ProductCustomerResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .stockQuantity(entity.getStockQuantity())
                .status(entity.getStatus())
                .build();
    }

}