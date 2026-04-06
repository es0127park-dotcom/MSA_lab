package com.metacoding.product.usecase;

import com.metacoding.product.core.handler.ex.*;
import com.metacoding.product.domain.Product;
import com.metacoding.product.repository.ProductRepository;
import com.metacoding.product.web.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService implements GetProductUseCase, GetAllProductsUseCase, DecreaseQuantityUseCase, IncreaseQuantityUseCase {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse findById(int productId) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new Exception404("상품이 없습니다."));
        return ProductResponse.from(findProduct);
    }

    @Override
    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse decreaseQuantity(int productId, int quantity, Long price) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new Exception404("상품이 없습니다."));
        // 재고 부족 검증
        findProduct.validateQuantity(quantity);
        // 가격 일치 검증
        findProduct.validatePrice(price);
        findProduct.decreaseQuantity(quantity);
        return ProductResponse.from(findProduct);
    }

    @Override
    @Transactional
    public ProductResponse increaseQuantity(int productId, int quantity, Long price) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new Exception404("상품이 없습니다."));
        // 가격 일치 검증
        findProduct.validatePrice(price);
        findProduct.increaseQuantity(quantity);
        return ProductResponse.from(findProduct);
    }
}
