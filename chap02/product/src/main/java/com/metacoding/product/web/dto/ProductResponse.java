package com.metacoding.product.web.dto;

import com.metacoding.product.domain.Product;

public record ProductResponse(
    int id,
    String productName,
    int quantity,
    Long price
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getProductName(),
            product.getQuantity(),
            product.getPrice()
        );
    }
}
