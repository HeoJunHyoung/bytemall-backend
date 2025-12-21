package com.example.bytemallbackend.domain.catalog.product.dto.response;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.entity.enumerate.ProductStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ProductCustomerDetailsResponse {

    private Long id;
    private String name;
    private Integer price;
    private Integer stockQuantity;
    private String description;
    private ProductStatus status;

    @Builder.Default
    private List<OptionResponse> options;

    public static ProductCustomerDetailsResponse fromEntity(Product entity) {
        return ProductCustomerDetailsResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .options(entity.getOptions().stream()
                        .map(OptionResponse::new)
                        .collect(Collectors.toList()))
                .build();
    }

}
