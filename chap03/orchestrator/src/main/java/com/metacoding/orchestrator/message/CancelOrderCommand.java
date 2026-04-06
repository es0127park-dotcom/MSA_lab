package com.metacoding.orchestrator.message;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderCommand {
    private int orderId;
}
