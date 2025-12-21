package com.example.bytemallbackend.domain.catalog.product.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductCreateRequest {

    private String productName;

    private Integer price;

    private String description;

    private Long categoryId;

    private List<OptionDto> options;

}
