package com.example.bytemallbackend.domain.review.dto.response;

import com.example.bytemallbackend.domain.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReviewResponse {

    private Long reviewId;
    private String writerName;      // 마스킹된 작성자 이름 (예: 홍*동)
    private Integer rating;
    private String content;         // 본문 미리보기 (50자 제한)
    private String productOptionName;
    private LocalDateTime createdAt;

    // 생성자 (private)
    private ReviewResponse(Long reviewId, String writerName, Integer rating, String content, String productOptionName, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.writerName = writerName;
        this.rating = rating;
        this.content = content;
        this.productOptionName = productOptionName;
        this.createdAt = createdAt;
    }

    // Entity를 받아서 DTO로 변환하는 정적 팩토리 메서드
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                maskName(review.getWriter().getUsername()), // 1. 이름 마스킹 처리
                review.getRating(),
                truncateContent(review.getContent()),   // 2. 본문 글자수 자르기
                review.getProductOptionName(),
                review.getCreatedAt()
        );
    }

    // == 헬퍼 메서드: 이름 마스킹 (홍길동 -> 홍*동) == //
    private static String maskName(String name) {
        if (name == null || name.length() < 2) {
            return name;
        }
        // 두 글자 이름 (김철 -> 김*)
        if (name.length() == 2) {
            return name.charAt(0) + "*";
        }
        // 세 글자 이상 (홍길동 -> 홍*동, 남궁민수 -> 남*민수)
        // 가장 간단하게 가운데를 *로 가리거나, 첫 글자 뒤를 *로 처리
        return name.charAt(0) + "*" + name.substring(2);
    }

    // == 헬퍼 메서드: 본문 자르기 (50자 초과 시 "...") == //
    private static String truncateContent(String content) {
        if (content == null) return "";
        if (content.length() <= 50) {
            return content;
        }
        return content.substring(0, 50) + "...";
    }
}