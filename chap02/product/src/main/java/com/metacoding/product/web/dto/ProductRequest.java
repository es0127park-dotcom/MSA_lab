package com.metacoding.product.web.dto;

public record ProductRequest(
    int quantity,
    Long price
) {
}
