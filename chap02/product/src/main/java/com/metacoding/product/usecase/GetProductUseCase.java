package com.metacoding.product.usecase;

import com.metacoding.product.web.dto.ProductResponse;

public interface GetProductUseCase {
    ProductResponse findById(int productId);
}
