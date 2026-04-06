package com.metacoding.order.adapter.consumer;

import com.metacoding.order.adapter.message.*;
import com.metacoding.order.usecase.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCommandConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "complete-order-command", groupId = "order-service")
    public void completeOrderCommand(CompleteOrderCommand command) {
        orderService.completeOrder(command.getOrderId());
    }

    @KafkaListener(topics = "cancel-order-command", groupId = "order-service")
    public void cancelOrderCommand(CancelOrderCommand command) {
        orderService.cancelOrder(command.getOrderId());
    }
}
