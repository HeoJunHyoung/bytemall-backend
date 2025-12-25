package com.example.bytemallbackend.domain.catalog.product.entity;

import com.example.bytemallbackend.domain.catalog.category.entity.Category;
import com.example.bytemallbackend.domain.catalog.product.entity.enumerate.ProductStatus;
import com.example.bytemallbackend.domain.catalog.product.exception.ProductErrorCode;
import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.global.common.BaseEntity;
import com.example.bytemallbackend.global.error.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> options = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long reviewCount = 0L;

    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double averageRating = 0.0;

    //== 생성자 ==//
    protected Product() { }

    private Product(String name, Integer price, String description, ProductStatus status, Category category, Member seller) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.status = status;
        this.category = category;
        this.seller = seller;
    }

    public static Product of(String name, Integer price, String description, Category category, Member seller) {
        return new Product(name, price, description, ProductStatus.ON_SALE, category, seller);
    }

    //== 비즈니스 로직==//
    public void update(String name, Integer price, String description, ProductStatus status, Category category) {
        if (name != null && !name.isBlank()) this.name = name;
        if (price != null) this.price = price;
        if (description != null) this.description = description;
        if (status != null) this.status = status;
        if (category != null) this.category = category;
    }

    // 리뷰 통계 업데이트 메서드
    public void updateReviewStats(Long count, Double rating) {
        this.reviewCount = count;
        this.averageRating = rating;
    }

    // 연관관계 편의 메서드
    public void assignOption(ProductOption option) {
        this.options.add(option);
        option.assignProduct(this);
    }

    public Integer getTotalStock() {
        return this.options.stream()
                .mapToInt(ProductOption::getStockQuantity)
                .sum();
    }

}
