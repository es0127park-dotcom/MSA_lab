package com.metacoding.order.usecase;

import com.metacoding.order.web.dto.*;

public interface CreateOrderUseCase {
    OrderResponse createOrder(int userId, java.util.List<OrderRequest.OrderItemDTO> orderItems, String address);
}
