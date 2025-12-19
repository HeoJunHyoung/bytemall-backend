package com.example.bytemallbackend.domain.catalog.product.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductCreateRequest {

    private String productName;

    private Integer price;

    private Integer stockQuantity;

    private String description;

    private Long categoryId;

}
