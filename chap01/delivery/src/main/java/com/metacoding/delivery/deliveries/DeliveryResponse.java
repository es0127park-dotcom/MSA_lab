package com.metacoding.delivery.deliveries;

import java.time.LocalDateTime;

public record DeliveryResponse(
    int id,
    int orderId,
    String address,
    DeliveryStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
    ) {
        public static DeliveryResponse from(Delivery delivery) {
            return new DeliveryResponse(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getAddress(),
                delivery.getStatus(),
                delivery.getCreatedAt(),
                delivery.getUpdatedAt()
            );
        }
}
