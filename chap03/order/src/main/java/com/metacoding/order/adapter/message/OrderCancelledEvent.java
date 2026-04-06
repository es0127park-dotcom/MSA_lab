package com.metacoding.order.adapter.message;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelledEvent {
    private int orderId;
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
