package com.example.bytemallbackend.domain.review.controller;

import com.example.bytemallbackend.domain.review.dto.request.ReviewCreateRequest;
import com.example.bytemallbackend.domain.review.dto.response.ReviewDetailResponse;
import com.example.bytemallbackend.domain.review.dto.response.ReviewResponse;
import com.example.bytemallbackend.domain.review.service.ReviewService;
import com.example.bytemallbackend.global.security.principal.AuthMember;
import lombok.RequiredArgsConstructor;
import org.apache.http.protocol.HTTP;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 등록
     */
    @PostMapping
    public ResponseEntity<Void> createReview(@AuthenticationPrincipal AuthMember authMember,
                                             @RequestBody ReviewCreateRequest request) {
        reviewService.createReview(authMember.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 리뷰 목록 조회
     */
    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<Page<ReviewResponse>> getProductReviews(
            @PathVariable Long productId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ReviewResponse> response = reviewService.getReviewsByProduct(productId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * 리뷰 상세 조회
     */
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDetailResponse> getReviewDetail(@PathVariable Long reviewId) {
        ReviewDetailResponse response = reviewService.getReviewDetail(reviewId);
        return ResponseEntity.ok(response);
    }
}
