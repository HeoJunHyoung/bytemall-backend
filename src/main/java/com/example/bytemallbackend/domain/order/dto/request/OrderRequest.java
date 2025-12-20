package com.example.bytemallbackend.domain.order.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequest {

    private List<OrderItemRequest> orderItemRequests;

}
