package com.example.bytemallbackend.domain.catalog.product.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OptionDto {
    private String optionName;    // 예: "Red / L"
    private Integer extraPrice;   // 예: 0
    private Integer stockQuantity;// 예: 10
}
