package com.metacoding.product.adapter.consumer;

import com.metacoding.product.adapter.message.*;
import com.metacoding.product.adapter.producer.ProductEventProducer;
import com.metacoding.product.usecase.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCommandConsumer {

    private final ProductService productService;
    private final ProductEventProducer productEventProducer;

    @KafkaListener(topics = "decrease-product-command", groupId = "product-service")
    public void decreaseProductCommand(DecreaseProductCommand command) {
        boolean success = false;
        try {
            productService.decreaseQuantity(command.getProductId(), command.getQuantity(), command.getPrice());
            success = true;
        } catch (Exception e) {
            System.out.println("재고 감소 실패: " + e.getMessage());
        }
        productEventProducer.publishProductDecreased(
                new ProductDecreasedEvent(command.getOrderId(), command.getProductId(), command.getQuantity(), success));
    }

    @KafkaListener(topics = "increase-product-command", groupId = "product-service")
    public void increaseProductCommand(IncreaseProductCommand command) {
        productService.increaseQuantity(command.getProductId(), command.getQuantity(), command.getPrice());
    }
}
