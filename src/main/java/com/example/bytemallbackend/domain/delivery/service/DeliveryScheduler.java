package com.example.bytemallbackend.domain.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryScheduler {

    private final DeliveryService deliveryService;

    // 3분(180,000ms)마다 배송 상태 업데이트 실행
    @Scheduled(fixedDelay = 180000)
    public void scheduleDeliveryUpdate() {
        deliveryService.autoUpdateStatus();
    }

}