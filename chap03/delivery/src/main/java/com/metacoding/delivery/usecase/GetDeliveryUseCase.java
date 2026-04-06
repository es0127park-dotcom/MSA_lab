package com.metacoding.delivery.usecase;

import com.metacoding.delivery.web.dto.DeliveryResponse;

public interface GetDeliveryUseCase {
    DeliveryResponse findById(int deliveryId);
}
