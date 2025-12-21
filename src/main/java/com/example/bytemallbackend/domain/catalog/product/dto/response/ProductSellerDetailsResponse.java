package com.example.bytemallbackend.domain.catalog.product.dto.response;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.entity.enumerate.ProductStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ProductSellerDetailsResponse {

    private Long id;
    private String name;
    private Integer price;
    private ProductStatus status;
    private String description;
    private Long categoryId;
    private String categoryName;

    private List<OptionResponse> options;

    public static ProductSellerDetailsResponse fromEntity(Product entity) {
        return ProductSellerDetailsResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .description(entity.getDescription())
                .categoryId(entity.getCategory().getId())
                .categoryName(entity.getCategory().getName())
                .options(entity.getOptions().stream()
                        .map(OptionResponse::new)
                        .collect(Collectors.toList()))
                .build();
    }

}