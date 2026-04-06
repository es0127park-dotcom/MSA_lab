package com.metacoding.orchestrator.message;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeliveryCommand {
    private int orderId;
    private String address;
}











