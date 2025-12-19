package com.example.bytemallbackend.domain.catalog.product.dto.response;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.entity.enumerate.ProductStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCustomerDetailsResponse {

    private Long id;
    private String name;
    private Integer price;
    private Integer stockQuantity;
    private String description;
    private ProductStatus status;

    public static ProductCustomerDetailsResponse fromEntity(Product entity) {
        return ProductCustomerDetailsResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .stockQuantity(entity.getStockQuantity())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .build();
    }

}
