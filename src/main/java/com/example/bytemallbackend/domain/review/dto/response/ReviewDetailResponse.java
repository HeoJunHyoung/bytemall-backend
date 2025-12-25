package com.example.bytemallbackend.domain.review.dto.response;

import com.example.bytemallbackend.domain.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewDetailResponse {

    private Long reviewId;
    private String productName;
    private String writerName;
    private Integer rating;
    private String content;
    private String productOptionName;
    private LocalDateTime createdAt;

    public ReviewDetailResponse(Review review) {
        this.reviewId = review.getId();
        this.productName = review.getProduct().getName(); // 필요하면 추가
        this.writerName = maskName(review.getWriter().getUsername());
        this.rating = review.getRating();
        this.content = review.getContent(); // 전체 내용
        this.productOptionName = review.getProductOptionName();
        this.createdAt = review.getCreatedAt();
    }

    private String maskName(String name) {
        if (name == null || name.length() < 2) return name;
        return name.substring(0, 1) + "*" + name.substring(2);
    }
}