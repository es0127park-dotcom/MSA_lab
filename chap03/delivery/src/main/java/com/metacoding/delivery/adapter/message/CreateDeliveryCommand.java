package com.metacoding.delivery.adapter.message;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeliveryCommand {
    private int orderId;
    private String address;
}
