package com.metacoding.product.adapter.message;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDecreasedEvent {
    private int orderId;
    private int productId;
    private int quantity;
    private boolean success;
}
