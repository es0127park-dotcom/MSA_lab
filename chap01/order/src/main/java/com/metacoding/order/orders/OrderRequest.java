package com.metacoding.order.orders;

import java.util.List;

public record OrderRequest(
    List<OrderItemDTO> orderItems,
    String address
) {
    public record OrderItemDTO(
        int productId,
        int quantity,
        Long price
    ) {}
}