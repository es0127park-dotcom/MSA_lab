package com.metacoding.order.adapter.dto;

public record ProductRequest(
    int productId,
    int quantity,
    Long price
) {
}

    
