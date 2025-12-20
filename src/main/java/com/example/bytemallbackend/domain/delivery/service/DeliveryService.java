package com.example.bytemallbackend.domain.delivery.service;

import com.example.bytemallbackend.domain.delivery.entity.Delivery;
import com.example.bytemallbackend.domain.delivery.entity.enumerate.DeliveryStatus;
import com.example.bytemallbackend.domain.delivery.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public void autoUpdateStatus() {
        // READY -> SHIPPING
        List<Delivery> readyList = deliveryRepository.findAllByStatus(DeliveryStatus.READY);
        readyList.forEach(d -> d.updateStatus(DeliveryStatus.SHIPPING));

        // SHIPPING -> COMP
        List<Delivery> shippingList = deliveryRepository.findAllByStatus(DeliveryStatus.SHIPPING);
        shippingList.forEach(d -> d.updateStatus(DeliveryStatus.COMP));

        log.info("배송 상태 자동 업데이트 완료: READY -> SHIPPING({}건), SHIPPING -> COMP({}건)",
                readyList.size(), shippingList.size());
    }
}