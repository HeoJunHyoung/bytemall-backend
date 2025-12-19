package com.example.bytemallbackend.domain.catalog.product.entity;

import com.example.bytemallbackend.domain.catalog.category.entity.Category;
import com.example.bytemallbackend.domain.catalog.product.entity.enumerate.ProductStatus;
import com.example.bytemallbackend.domain.catalog.product.exception.ProductErrorCode;
import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.global.common.BaseEntity;
import com.example.bytemallbackend.global.error.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "products")
@Getter
public class Product extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Lob
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    protected Product() { }

    private Product(String name, Integer price, Integer stockQuantity, String description, ProductStatus status, Category category, Member seller) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.status = status;
        this.category = category;
        this.seller = seller;
    }

    public static Product of(String name, Integer price, Integer stockQuantity, String description, Category category, Member seller) {
        return new Product(name, price, stockQuantity, description, ProductStatus.ON_SALE, category, seller);
    }

    //== 비즈니스 로직==//
    public void update(String name, Integer price, Integer stockQuantity, String description, ProductStatus status, Category category) {
        if (name != null && !name.isBlank()) this.name = name;
        if (price != null) this.price = price;
        if (stockQuantity != null) this.stockQuantity = stockQuantity;
        if (description != null) this.description = description;
        if (status != null) this.status = status;
        if (category != null) this.category = category;
    }

    public void addStock(Integer quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(Integer quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new BusinessException(ProductErrorCode.PRODUCT_NOT_ENOUGH);
        }
        this.stockQuantity = restStock;
    }

}
