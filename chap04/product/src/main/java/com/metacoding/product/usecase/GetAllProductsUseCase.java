package com.metacoding.product.usecase;

import com.metacoding.product.web.dto.ProductResponse;

import java.util.List;

public interface GetAllProductsUseCase {
    List<ProductResponse> findAll();
}
