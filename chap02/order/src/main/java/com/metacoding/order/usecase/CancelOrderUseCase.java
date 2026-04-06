package com.metacoding.order.usecase;

import com.metacoding.order.web.dto.OrderResponse;

public interface CancelOrderUseCase {
    OrderResponse cancelOrder(int orderId);
}
