package com.metacoding.delivery.usecase;

import com.metacoding.delivery.adapter.message.DeliveryCompletedEvent;
import com.metacoding.delivery.adapter.producer.DeliveryEventProducer;
import com.metacoding.delivery.core.handler.ex.*;
import com.metacoding.delivery.domain.Delivery;
import com.metacoding.delivery.repository.DeliveryRepository;
import com.metacoding.delivery.web.dto.DeliveryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DeliveryService implements CreateDeliveryUseCase, GetDeliveryUseCase, CancelDeliveryUseCase, CompleteDeliveryUseCase {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryEventProducer deliveryEventProducer;

    @Override
    @Transactional
    public DeliveryResponse createDelivery(int orderId, String address) {
        // 1. 배달 생성
        Delivery createdDelivery = deliveryRepository.save(Delivery.create(orderId, address));
        // 2. 주소 검증
        Delivery.validateAddress(address);
        return DeliveryResponse.from(createdDelivery);
    }

    @Override
    public DeliveryResponse findById(int deliveryId) {
        Delivery findDelivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new Exception404("배달 정보를 조회할 수 없습니다."));
        return DeliveryResponse.from(findDelivery);
    }

    @Override
    @Transactional
    public DeliveryResponse cancelDelivery(int orderId) {
        Delivery findDelivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new Exception404("배달 정보를 조회할 수 없습니다."));
        findDelivery.cancel();
        return DeliveryResponse.from(findDelivery);
    }

    @Override
    @Transactional
    public DeliveryResponse completeDelivery(int deliveryId) {
        Delivery findDelivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new Exception404("배달 정보를 조회할 수 없습니다."));
        findDelivery.complete();
        deliveryEventProducer.publishDeliveryCompleted(new DeliveryCompletedEvent(findDelivery.getOrderId()));
        return DeliveryResponse.from(findDelivery);
    }
}
