package com.metacoding.order.adapter.dto;

public record DeliveryRequest(
    int orderId,
    String address
) {
}


