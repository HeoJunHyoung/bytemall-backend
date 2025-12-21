package com.example.bytemallbackend.domain.order.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequest {

    // 배송지 선택 정보
    private Long deliveryAddressId; // 기존 주소록 사용 시
    private String zipcode;         // 직접 입력 시
    private String roadAddress;
    private String detailAddress;

    private List<OrderItemRequest> orderItemRequests;

    private boolean fromCart;
}
