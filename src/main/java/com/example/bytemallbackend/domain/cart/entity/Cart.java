package com.example.bytemallbackend.domain.cart.entity;


import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.entity.ProductOption;
import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "carts")
@Getter
public class Cart extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    // 생성자
    protected Cart() { }

    private Cart(Member member) {
        this.member = member;
    }

    public static Cart createCart(Member member) {
        return new Cart(member);
    }

    // 연관관계 편의 메서드
    public void assignCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.assignCart(this);
    }

    // 비즈니스 로직
    public void addCartItem(Product product, ProductOption option, Integer count) {

        // 동일한 '옵션'이 이미 장바구니에 있는지 확인
        Optional<CartItem> existingItem = this.cartItems.stream()
                .filter(item -> item.getProductOption().getId().equals(option.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().addCount(count);
        } else {
            // 새 아이템 생성 시 옵션 객체 전달
            CartItem newItem = CartItem.createCartItem(product, option, count);
            this.assignCartItem(newItem);
        }
    }

}
