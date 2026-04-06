package com.metacoding.product.web;

import com.metacoding.product.usecase.*;
import com.metacoding.product.web.dto.ProductRequest;
import org.springframework.http.ResponseEntity;
import com.metacoding.product.core.util.Resp;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final GetProductUseCase getProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final DecreaseQuantityUseCase decreaseQuantityUseCase;
    private final IncreaseQuantityUseCase increaseQuantityUseCase;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable("productId") int productId) {
        return Resp.ok(getProductUseCase.findById(productId));
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return Resp.ok(getAllProductsUseCase.findAll());
    }

    @PutMapping("/{productId}/decrease")
    public ResponseEntity<?> decreaseQuantity(@PathVariable("productId") int productId, @RequestBody ProductRequest requestDTO) {
        return Resp.ok(decreaseQuantityUseCase.decreaseQuantity(productId, requestDTO.quantity(), requestDTO.price()));
    }

    @PutMapping("/{productId}/increase")
    public ResponseEntity<?> increaseQuantity(@PathVariable("productId") int productId, @RequestBody ProductRequest requestDTO) {
        return Resp.ok(increaseQuantityUseCase.increaseQuantity(productId, requestDTO.quantity(), requestDTO.price()));
    }
}
