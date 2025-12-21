package com.example.bytemallbackend.domain.catalog.product.dto.response;

import com.example.bytemallbackend.domain.catalog.product.entity.ProductOption;
import lombok.Data;

@Data
public class OptionResponse {
    private Long id;
    private String optionName;
    private Integer extraPrice;
    private Integer stockQuantity;

    public OptionResponse(ProductOption option) {
        this.id = option.getId();
        this.optionName = option.getOptionName();
        this.extraPrice = option.getExtraPrice();
        this.stockQuantity = option.getStockQuantity();
    }
}