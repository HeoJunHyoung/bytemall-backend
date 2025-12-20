package com.example.bytemallbackend.domain.delivery.entity;

import com.example.bytemallbackend.domain.delivery.entity.enumerate.DeliveryStatus;
import com.example.bytemallbackend.domain.order.entity.Order;
import com.example.bytemallbackend.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "deliveries")
public class Delivery extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private Delivery(Address address, DeliveryStatus status) {
        this.address = address;
        this.status = status;
    }

    // 생성자
    protected Delivery() { }

    public static Delivery createDelivery(Address address) {
        return new Delivery(address, DeliveryStatus.READY);
    }

    // 연관관계 편의 메서드
    public void assignOrder(Order order) {
        this.order = order;
    }

    // 비즈니스 로직
    public void updateStatus(DeliveryStatus status) {
        this.status = status;
    }
}