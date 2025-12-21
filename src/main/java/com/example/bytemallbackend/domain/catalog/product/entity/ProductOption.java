package com.example.bytemallbackend.domain.catalog.product.entity;

import com.example.bytemallbackend.domain.catalog.product.exception.ProductErrorCode;
import com.example.bytemallbackend.global.error.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "product_options")
@Getter
public class ProductOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, name = "option_name")
    private String optionName;

    @Column(name = "extra_price")
    private Integer extraPrice;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    // 생성자
    protected ProductOption() { }

    private ProductOption(Product product, String optionName, Integer extraPrice, Integer stockQuantity) {
        this.product = product;
        this.optionName = optionName;
        this.extraPrice = extraPrice;
        this.stockQuantity = stockQuantity;
    }

    public static ProductOption createOption(Product product, String optionName, Integer extraPrice, Integer stockQuantity) {
        return new ProductOption(product, optionName, extraPrice, stockQuantity);
    }

    // 연관관계 편의 메서드
    public void assignProduct(Product product) {
        this.product = product;
    }

    // 비즈니스 로직
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new BusinessException(ProductErrorCode.PRODUCT_NOT_ENOUGH);
        }
        this.stockQuantity = restStock;
    }

}
