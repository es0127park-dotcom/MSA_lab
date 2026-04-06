package com.metacoding.orchestrator.message;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private int orderId;
    private int userId;
    private String address;
    private List<OrderItem> orderItems;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private int productId;
        private int quantity;
        private Long price;
    }
}
