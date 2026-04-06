package com.metacoding.orchestrator.handler;

import com.metacoding.orchestrator.message.*; 
import lombok.*; 
import org.springframework.kafka.annotation.KafkaListener; 
import org.springframework.kafka.core.KafkaTemplate; 
import org.springframework.stereotype.Component; 

import java.util.*;
import java.util.concurrent.ConcurrentHashMap; 

@Component 
@RequiredArgsConstructor 
public class OrderOrchestrator {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Map<Integer, WorkflowState> states = new ConcurrentHashMap<>(); 

    // 주문 생성 이벤트 수신 → 재고 차감 명령 발행
    @KafkaListener(topics = "order-created", groupId = "orchestrator")
    public void orderCreated(OrderCreatedEvent event) {
        int orderId = event.getOrderId(); 
        List<OrderCreatedEvent.OrderItem> items = List.copyOf(event.getOrderItems()); 
        // 주문 상태 저장
        states.put(orderId, new WorkflowState(orderId, event.getAddress(), items)); 

        // 상품별 재고 차감 명령 발행
        for (OrderCreatedEvent.OrderItem item : items) {
            kafkaTemplate.send(
                    "decrease-product-command", 
                    String.valueOf(orderId), 
                    new DecreaseProductCommand(
                            orderId,
                            item.getProductId(),
                            item.getQuantity(),
                            item.getPrice()
                    )
            );
        }
    }

    // 재고 차감 결과 수신 → 전부 성공 시 배달 생성 
    @KafkaListener(topics = "product-decreased", groupId = "orchestrator")
    public void productDecreased(ProductDecreasedEvent event) {
        int orderId = event.getOrderId(); 
        // 주문 상태 조회
        WorkflowState state = states.get(orderId); 
        // 주문 상태 없으면 종료
        if (state == null) return; 

        // 실패 시: 이미 성공했던 재고만 복구
        if (!event.isSuccess()) {
            for (OrderCreatedEvent.OrderItem item : state.items) {
                if (state.decreasedProductIds.contains(item.getProductId())) {
                    kafkaTemplate.send(
                            "increase-product-command", 
                            String.valueOf(orderId),
                            new IncreaseProductCommand(
                                    orderId,
                                    item.getProductId(),
                                    item.getQuantity(),
                                    item.getPrice()
                            )
                    );
                }
            }
            // 주문 취소 명령 발행
            kafkaTemplate.send(
                    "cancel-order-command",
                    String.valueOf(orderId),
                    new CancelOrderCommand(orderId)
            );

            states.remove(orderId);
            return;
        }

        // 성공 시: 성공한 상품 기록
        state.decreasedProductIds.add(event.getProductId());
        state.processed++; 

        // 모든 상품 재고 차감 완료 시에만 배달 생성
        if (state.processed == state.getItems().size()) {
            kafkaTemplate.send(
                    "create-delivery-command", 
                    String.valueOf(orderId),
                    new CreateDeliveryCommand(orderId, state.address)
            );
        }
    }

    // 배달 생성 결과 수신 (실패 시에만 재고 복구, 성공 시에는 배달 완료 API 대기)
    @KafkaListener(topics = "delivery-created", groupId = "orchestrator")
    public void deliveryCreated(DeliveryCreatedEvent event) {
        int orderId = event.getOrderId();
        WorkflowState state = states.get(orderId);
        if (state == null) return;

        if (!event.isSuccess()) {
            for (OrderCreatedEvent.OrderItem item : state.items) {
                if (state.decreasedProductIds.contains(item.getProductId())) {
                    kafkaTemplate.send(
                            "increase-product-command",
                            String.valueOf(orderId),
                            new IncreaseProductCommand(
                                    orderId,
                                    item.getProductId(),
                                    item.getQuantity(),
                                    item.getPrice()
                            )
                    );
                }
            }
            // 주문 취소 명령 발행
            kafkaTemplate.send(
                    "cancel-order-command",
                    String.valueOf(orderId),
                    new CancelOrderCommand(orderId)
            );
            states.remove(orderId);
            return;
        }
        states.remove(orderId);
    }

    // 배달 완료 이벤트 수신 → 주문 완료 명령 발행
    @KafkaListener(topics = "delivery-completed", groupId = "orchestrator")
    public void deliveryCompleted(DeliveryCompletedEvent event) {
        kafkaTemplate.send(
                "complete-order-command",
                String.valueOf(event.getOrderId()),
                new CompleteOrderCommand(event.getOrderId())
        );
    }

    @Data
    private static class WorkflowState {
        private final int orderId;
        private final String address;
        private final List<OrderCreatedEvent.OrderItem> items;
        private int processed = 0;
        // 재고 차감감 성공한 상품 ID 
        private final Set<Integer> decreasedProductIds = ConcurrentHashMap.newKeySet();
    }
}
