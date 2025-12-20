package com.example.bytemallbackend.domain.order.entity;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
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

    private Integer price;
    private Integer count;

    private OrderItem(Product product, Integer price, Integer count) {
        this.product = product;
        this.price = price;
        this.count = count;
    }

    // 생성자
    protected OrderItem() { }

    public static OrderItem createOrderItem(Product product,Integer count) {
        product.removeStock(count);
        return new OrderItem(product, product.getPrice(), count);
    }

    // 연관관계 메서드
    public void assignOrder(Order order) {
        this.order = order;
    }

    // 비즈니스 로직
    public void cancel() {
        this.product.addStock(count);
    }

    public Integer getTotalPrice() {
        return this.price * this.count;
    }

}
