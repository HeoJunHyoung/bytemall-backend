package com.example.bytemallbackend.domain.order.repository;

import com.example.bytemallbackend.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("select oi from OrderItem oi " +
            "join fetch oi.order o " +
            "join fetch o.member m " +
            "join fetch o.delivery d " +
            "left join fetch oi.productOption po " +
            "where oi.id = :id")
    Optional<OrderItem> findByIdWithOrderAndMember(@Param("id") Long id);
}
