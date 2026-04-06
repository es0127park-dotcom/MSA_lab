package com.metacoding.delivery.adapter.producer;

import com.metacoding.delivery.adapter.message.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishDeliveryCreated(DeliveryCreatedEvent event) {
        kafkaTemplate.send("delivery-created", event);
        System.out.println("delivery 이벤트 생성");
    }
}
