package com.example.bytemallbackend.domain.order.entity;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.entity.ProductOption;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "order_item")
@Getter
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    private Integer orderPrice;
    private Integer count;

    // 생성자
    protected OrderItem() { }

    private OrderItem(Product product, Integer orderPrice, Integer count) {
        this.product = product;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public static OrderItem createOrderItem(Product product, ProductOption option, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.product = product;
        orderItem.productOption = option;
        orderItem.count = count;

        // 수총액이 아닌 '단가(1개 가격)'
        // ㄴ (상품 기본가 + 옵션 추가금)
        orderItem.orderPrice = product.getPrice() + (option != null ? option.getExtraPrice() : 0);

        // 재고 차감 로직
        if (option != null) {
            option.removeStock(count);
        }

        return orderItem;
    }

    // 연관관계 메서드
    public void assignOrder(Order order) {
        this.order = order;
    }

    //== 비즈니스 로직 ==//
    //  주문 취소 시 재고 복구 로직
    public void cancel() {
        if (this.productOption != null) {
            this.productOption.addStock(count);
        }
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

}
