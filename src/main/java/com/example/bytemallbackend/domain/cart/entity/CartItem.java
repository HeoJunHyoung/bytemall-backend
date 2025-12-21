package com.example.bytemallbackend.domain.cart.entity;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.entity.ProductOption;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption;

    private Integer count;

    protected CartItem() { }

    private CartItem(Product product, ProductOption productOption, Integer count) {
        this.product = product;
        this.productOption = productOption;
        this.count = count;
    }

    public static CartItem createCartItem(Product product, ProductOption option, Integer count) {
        return new CartItem(product, option, count);
    }

    public void assignCart(Cart cart) {
        this.cart = cart;
    }

    public void addCount(Integer count) {
        this.count += count;
    }

    public void updateCount(Integer count) {
        this.count = count;
    }
}