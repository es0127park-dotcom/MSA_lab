package com.metacoding.orchestrator.message;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCompletedEvent {
    private int orderId;
}
