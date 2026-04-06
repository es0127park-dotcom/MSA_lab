package com.metacoding.delivery.usecase;

import com.metacoding.delivery.web.dto.DeliveryResponse;

public interface CompleteDeliveryUseCase {
    DeliveryResponse completeDelivery(int deliveryId);
}
