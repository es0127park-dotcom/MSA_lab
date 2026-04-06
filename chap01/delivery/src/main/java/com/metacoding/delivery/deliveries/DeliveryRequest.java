package com.metacoding.delivery.deliveries;

public record DeliveryRequest(
    int orderId,
    String address
) {
}
