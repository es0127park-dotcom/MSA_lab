package com.metacoding.order.web.dto;

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
