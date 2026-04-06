package com.metacoding.product.usecase;

import com.metacoding.product.web.dto.ProductResponse;

public interface DecreaseQuantityUseCase {
    ProductResponse decreaseQuantity(int productId, int quantity, Long price);
}
