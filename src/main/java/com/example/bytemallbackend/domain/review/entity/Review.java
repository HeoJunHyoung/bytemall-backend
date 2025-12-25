package com.example.bytemallbackend.domain.review.entity;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.domain.order.entity.OrderItem;
import com.example.bytemallbackend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "reviews")
@Getter
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false, unique = true)
    private OrderItem orderItem; // 어떤 "주문 상품"에 대한 리뷰인지 (구매 인증 & 중복 방지 & 옵션 확인용)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product; // 조회 성능을 위해 유지 (OrderItem 타고 들어가도 되지만, 바로 접근하기 위함)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer; // 작성자

    @Column(nullable = false)
    private Integer rating; // 별점 (1~5)

    @Column(columnDefinition = "TEXT")
    private String content; // 리뷰 내용

    // 옵션 정보를 문자열로 선언 (스냅샷 용도 ; 조인 없이 바로 출력)
    private String productOptionName;

    // == 생성자 (private) == //
    protected Review() {
    }

    private Review(OrderItem orderItem, Member writer, Integer rating, String content) {
        this.orderItem = orderItem;
        this.writer = writer;
        this.rating = rating;
        this.content = content;
        this.product = orderItem.getProduct();
        if (orderItem.getProductOption() != null) {
            this.productOptionName = orderItem.getProductOption().getOptionName();
        } else {
            this.productOptionName = null;
        }
    }

    // == 생성 메서드 (Factory Method) == //
    public static Review createReview(OrderItem orderItem, Member writer, Integer rating, String content) {
        return new Review(orderItem, writer, rating, content);
    }

}
