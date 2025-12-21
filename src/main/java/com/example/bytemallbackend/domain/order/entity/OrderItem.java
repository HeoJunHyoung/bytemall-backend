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

    private Integer price;
    private Integer count;

    private OrderItem(Product product, Integer price, Integer count) {
        this.product = product;
        this.price = price;
        this.count = count;
    }

    // 생성자
    protected OrderItem() { }

    public static OrderItem createOrderItem(Product product, ProductOption option, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.product = product;
        orderItem.productOption = option;
        orderItem.count = count;

        // 가격 계산: (상품기본가 + 옵션추가금) * 수량
        int price = product.getPrice() + (option != null ? option.getExtraPrice() : 0);
        orderItem.price = price * count;

        // 상품 재고가 아니라 '옵션 재고'를 차감
        if (option != null) {
            option.removeStock(count);
        }

        return orderItem;
    }

    //  주문 취소 시 재고 복구 로직
    public void cancel() {
        if (this.productOption != null) {
            this.productOption.addStock(count);
        }
    }

    // 연관관계 메서드
    public void assignOrder(Order order) {
        this.order = order;
    }

}
