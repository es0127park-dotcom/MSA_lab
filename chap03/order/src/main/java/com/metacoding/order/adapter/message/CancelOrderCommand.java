package com.metacoding.order.adapter.message;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderCommand {
    private int orderId;
}
