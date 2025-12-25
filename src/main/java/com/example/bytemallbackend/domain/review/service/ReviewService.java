package com.example.bytemallbackend.domain.review.service;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.exception.ProductErrorCode;
import com.example.bytemallbackend.domain.catalog.product.repository.ProductRepository;
import com.example.bytemallbackend.domain.delivery.entity.enumerate.DeliveryStatus;
import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.domain.member.repository.MemberRepository;
import com.example.bytemallbackend.domain.order.entity.OrderItem;
import com.example.bytemallbackend.domain.order.exception.OrderErrorCode;
import com.example.bytemallbackend.domain.order.repository.OrderItemRepository;
import com.example.bytemallbackend.domain.order.repository.OrderRepository;
import com.example.bytemallbackend.domain.review.dto.request.ReviewCreateRequest;
import com.example.bytemallbackend.domain.review.dto.response.ReviewDetailResponse;
import com.example.bytemallbackend.domain.review.dto.response.ReviewResponse;
import com.example.bytemallbackend.domain.review.entity.Review;
import com.example.bytemallbackend.domain.review.exception.ReviewErrorCode;
import com.example.bytemallbackend.domain.review.repository.ReviewRepository;
import com.example.bytemallbackend.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /**
     * 리뷰 작성
     */
    @Transactional
    public void createReview(Long memberId, ReviewCreateRequest request) {
        // 1. 주문 상품 조회
        OrderItem orderItem = orderItemRepository.findByIdWithOrderAndMember(request.getOrderItemId())
                .orElseThrow(() -> new BusinessException(ReviewErrorCode.ORDER_ITEM_NOT_FOUND));

        // 2. 권한 검증: 리뷰 작성자가 실제 주문자인지 확인
        if (!orderItem.getOrder().getMember().getId().equals(memberId)) {
            throw new BusinessException(ReviewErrorCode.REVIEW_PERMISSION_DENIED);
        }

        // 3. 배송 상태 검증: 배송 완료(COMP)된 상품만 리뷰 작성 가능
//        if (orderItem.getOrder().getDelivery().getStatus() != DeliveryStatus.COMP) {
//            throw new BusinessException(ReviewErrorCode.REVIEW_NOT_DELIVERED);
//        }

        // 4. 중복 작성 검증
        if (reviewRepository.existsByOrderItem(orderItem)) {
            throw new BusinessException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
        }

        // 5. 작성자 조회
        Member writer = memberRepository.getReferenceById(memberId);

        Review review = Review.createReview(
                orderItem,
                writer,
                request.getRating(),
                request.getContent()
        );

        // 6. 리뷰 생성
        reviewRepository.save(review);

        // 7. 리뷰 작성 후 상품 테이블의 통계 정보 업데이트
        updateProductStatistics(review.getProduct());
    }

    /**
     * 리뷰 목록 조회
     */
    public Page<ReviewResponse> getReviewsByProduct(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));

        return reviewRepository.findByProduct(product, pageable)
                .map(ReviewResponse::from);
    }

    /**
     * 리뷰 상세 조회
     */
    public ReviewDetailResponse getReviewDetail(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ReviewErrorCode.REVIEW_NOT_FOUND));

        return new ReviewDetailResponse(review);
    }

    //== 헬퍼 메서드 ==//
    // 통계 업데이트 헬퍼 메서드
    private void updateProductStatistics(Product product) {
        long count = reviewRepository.countByProduct(product);
        Double avg = reviewRepository.getAverageRatingByProduct(product);

        // 소수점 첫째 자리까지만 반올림
        avg = Math.round(avg * 10) / 10.0;

        product.updateReviewStats(count, avg);
    }
}
