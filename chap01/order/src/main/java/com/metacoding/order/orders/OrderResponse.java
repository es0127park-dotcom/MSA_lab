package com.metacoding.order.orders;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    int id,
    int userId,
    OrderStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<OrderItem> orderItems
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getUserId(),
            order.getStatus(),
            order.getCreatedAt(),
            order.getUpdatedAt(),
            List.of()
        );
    }

    public static OrderResponse from(Order order, List<OrderItem> orderItems) {
        return new OrderResponse(
            order.getId(),
            order.getUserId(),
            order.getStatus(),
            order.getCreatedAt(),
            order.getUpdatedAt(),
            orderItems
        );
    }
}