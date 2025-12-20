package com.example.bytemallbackend.domain.delivery.repository;

import com.example.bytemallbackend.domain.delivery.entity.Delivery;
import com.example.bytemallbackend.domain.delivery.entity.enumerate.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {


    List<Delivery> findAllByStatus(DeliveryStatus status);

}
