package com.metacoding.delivery.adapter.message;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCompletedEvent {
    private int orderId;
}
