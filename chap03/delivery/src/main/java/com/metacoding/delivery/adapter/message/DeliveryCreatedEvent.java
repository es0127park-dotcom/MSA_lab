package com.metacoding.delivery.adapter.message;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCreatedEvent {
    private int orderId;
    private int deliveryId;
    private boolean success;
}
