package com.metacoding.delivery.web.dto;

public record DeliveryRequest(
    int orderId,
    String address
) {
}
