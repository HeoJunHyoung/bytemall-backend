package com.example.bytemallbackend.domain.cart.entity;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "cart_items")
@Getter
public class CartItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer count;

    // 생성자
    protected CartItem() { }

    private CartItem(Product product, Integer count) {
        this.product = product;
        this.count = count;
    }

    public static CartItem createCartItem(Product product, Integer count) {
        return new CartItem(product, count);
    }

    // 연관관계 편의 메서드
    public void assignCart(Cart cart) {
        this.cart = cart;
    }

    public void addCount(Integer count) {
        this.count += count;
    }

    // 비즈니스 로직
    public void updateCount(Integer count) {
        this.count = count;
    }
}
