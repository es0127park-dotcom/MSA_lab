package com.metacoding.delivery.adapter.consumer;

import com.metacoding.delivery.adapter.message.*;
import com.metacoding.delivery.adapter.producer.DeliveryEventProducer;
import com.metacoding.delivery.usecase.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryCommandConsumer {

    private final DeliveryService deliveryService;
    private final DeliveryEventProducer deliveryEventProducer;

    @KafkaListener(topics = "create-delivery-command", groupId = "delivery-service")
    public void createDeliveryCommand(CreateDeliveryCommand command) {
        boolean success = false;
        int deliveryId = 0;
        try {
            var response = deliveryService.createDelivery(command.getOrderId(), command.getAddress());
            success = true;
            deliveryId = response.id();
        } catch (Exception e) {
            System.out.println("배달 생성 실패: " + e.getMessage());
        }
        deliveryEventProducer.publishDeliveryCreated(
                new DeliveryCreatedEvent(command.getOrderId(), deliveryId, success));
    }

    @KafkaListener(topics = "cancel-delivery-command", groupId = "delivery-service")
    public void cancelDeliveryCommand(CancelDeliveryCommand command) {
        deliveryService.cancelDelivery(command.getOrderId());
    }
}
