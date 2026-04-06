package com.metacoding.order.usecase;

import com.metacoding.order.web.dto.OrderResponse;

public interface GetOrderUseCase {
    OrderResponse findById(int orderId);
}
