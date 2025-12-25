package com.example.bytemallbackend.domain.review.repository;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.order.entity.OrderItem;
import com.example.bytemallbackend.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 이미 작성된 리뷰가 있는지 확인
    boolean existsByOrderItem(OrderItem orderItem);

    // 상품별 리뷰 조회
    Page<Review> findByProduct(Product product, Pageable pageable);

    // 특정 상품의 리뷰 개수 조회
    long countByProduct(Product product);

    // 특정 상품의 별점 평균 조회 (결과가 없으면 null 반환 가능성 있음)
    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.product = :product")
    Double getAverageRatingByProduct(@Param("product") Product product);
}
