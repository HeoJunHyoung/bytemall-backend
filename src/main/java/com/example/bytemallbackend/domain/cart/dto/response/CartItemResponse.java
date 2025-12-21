package com.example.bytemallbackend.domain.cart.dto.response;

import com.example.bytemallbackend.domain.cart.entity.CartItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItemResponse {

    private Long cartItemId;
    private Long productId;
    private String productName;

    // 옵션 정보 추가
    private Long optionId;
    private String optionName;
    private Integer extraPrice;

    private Integer count;
    private Integer unitPrice;  // (상품 기본가 + 옵션 추가금)
    private Integer totalPrice; // (unitPrice * 수량)

    // 생성자
    private CartItemResponse(Long cartItemId, Long productId, String productName,
                             Long optionId, String optionName, Integer extraPrice,
                             Integer count, Integer unitPrice, Integer totalPrice) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.optionId = optionId;
        this.optionName = optionName;
        this.extraPrice = extraPrice;
        this.count = count;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    public static CartItemResponse fromEntity(CartItem entity) {
        // 1. 가격 계산 로직
        int basePrice = entity.getProduct().getPrice();
        int extra = (entity.getProductOption() != null) ? entity.getProductOption().getExtraPrice() : 0;
        int unitPrice = basePrice + extra;

        // 2. 생성자를 통해 객체 생성 및 반환
        return new CartItemResponse(
                entity.getId(),
                entity.getProduct().getId(),
                entity.getProduct().getName(),
                (entity.getProductOption() != null) ? entity.getProductOption().getId() : null,
                (entity.getProductOption() != null) ? entity.getProductOption().getOptionName() : "기본 옵션",
                extra,
                entity.getCount(),
                unitPrice,
                unitPrice * entity.getCount()
        );
    }
}