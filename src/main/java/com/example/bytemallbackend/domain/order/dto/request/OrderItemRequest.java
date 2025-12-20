package com.example.bytemallbackend.domain.order.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemRequest {

    private Long productId;

    private Integer count;

}
