package com.metacoding.delivery.usecase;

import com.metacoding.delivery.web.dto.DeliveryResponse;

public interface CancelDeliveryUseCase {
    DeliveryResponse cancelDelivery(int orderId);
}
