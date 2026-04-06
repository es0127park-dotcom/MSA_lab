package com.metacoding.product.products;

import com.metacoding.product.core.handler.ex.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse findById(int productId) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new Exception404("상품이 없습니다."));
        return ProductResponse.from(findProduct);
    }

    @Transactional
    public ProductResponse decreaseQuantity(int productId, int quantity, Long price) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new Exception404("상품이 없습니다."));
        if (findProduct.getQuantity() < quantity) {
            throw new Exception400("상품 재고가 부족합니다.");
        }
        if (!price.equals(findProduct.getPrice())) {
            throw new Exception400("상품 가격이 일치하지 않습니다.");
        }
        findProduct.decreaseQuantity(quantity);
        return ProductResponse.from(findProduct);
    }

    @Transactional
    public ProductResponse increaseQuantity(int productId, int quantity, Long price) {
        Product findProduct = productRepository.findById(productId)
                .orElseThrow(() -> new Exception404("상품이 없습니다."));
        if (!price.equals(findProduct.getPrice())) {
            throw new Exception400("상품 가격이 일치하지 않습니다.");
        }
        findProduct.increaseQuantity(quantity);
        return ProductResponse.from(findProduct);
    }
}
