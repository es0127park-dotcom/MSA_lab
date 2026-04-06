package com.metacoding.delivery.usecase;

import com.metacoding.delivery.web.dto.DeliveryResponse;

public interface SaveDeliveryUseCase {
    DeliveryResponse saveDelivery(int orderId, String address);
}
