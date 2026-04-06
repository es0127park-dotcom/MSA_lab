package com.metacoding.delivery.deliveries;

import com.metacoding.delivery.core.handler.ex.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public DeliveryResponse createDelivery(int orderId, String address) {
        // 1. 배달 생성
        Delivery createdDelivery = deliveryRepository.save(Delivery.create(orderId, address));
        // 2. 주소 검증
        if (address == null || address.isBlank()) {
            throw new Exception400("배달 주소는 필수입니다.");
        }
        // 3. 배달 완료
        createdDelivery.complete();
        return DeliveryResponse.from(createdDelivery);
    }

    public DeliveryResponse findById(int deliveryId) {
        Delivery findDelivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new Exception404("배달 정보를 조회할 수 없습니다."));
        return DeliveryResponse.from(findDelivery);
    }

    @Transactional
    public DeliveryResponse cancelDelivery(int orderId) {
        Delivery findDelivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new Exception404("배달 정보를 조회할 수 없습니다."));
        if(findDelivery.getStatus() == DeliveryStatus.CANCELLED) {
            throw new Exception400("배달이 이미 취소되었습니다.");
        }
        findDelivery.cancel();
        return DeliveryResponse.from(findDelivery);
    }
}