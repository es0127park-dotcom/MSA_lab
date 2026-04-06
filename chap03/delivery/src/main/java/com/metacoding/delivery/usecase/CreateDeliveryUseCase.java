package com.metacoding.delivery.usecase;

import com.metacoding.delivery.web.dto.DeliveryResponse;

public interface CreateDeliveryUseCase {
    DeliveryResponse createDelivery(int orderId, String address);
}
