package com.example.bytemallbackend.domain.order.entity;

import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.domain.order.entity.enumerate.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    /**
     * 생성자
     */
    protected Order() { }

    public static Order createOrder(Member member, OrderStatus status, List<OrderItem> orderItems) {
        Order order = new Order();

        order.member = member;
        order.status = status;
        order.orderDate = LocalDateTime.now();

        for (OrderItem orderItem : orderItems) {
            order.assignOrderItem(orderItem);
        }

        return order;
    }

    /**
     * 연관관계 메서드
     */
    public void assignOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }

    /**
     * 비즈니스 로직
     */
    // 주문 취소
    public void cancel() {
        this.status = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // 전체 주문 가격 조회
    public Integer getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
    
}
