package com.metacoding.product.usecase;

import com.metacoding.product.web.dto.ProductResponse;

public interface IncreaseQuantityUseCase {
    ProductResponse increaseQuantity(int productId, int quantity, Long price);
}
