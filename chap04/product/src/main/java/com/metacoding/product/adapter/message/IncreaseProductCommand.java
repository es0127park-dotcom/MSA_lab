package com.metacoding.product.adapter.message;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IncreaseProductCommand {
    private int orderId;
    private int productId;
    private int quantity;
    private long price;
}
