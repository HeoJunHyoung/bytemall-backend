package com.example.bytemallbackend.domain.order.repository;

import com.example.bytemallbackend.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 내가 주문한 주문 조회
    @Query("SELECT o FROM Order o JOIN FETCH o.member m WHERE m.id = :memberId")
    Page<Order> findAllByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
