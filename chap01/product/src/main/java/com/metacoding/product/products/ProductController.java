package com.metacoding.product.products;

import org.springframework.http.ResponseEntity;
import com.metacoding.product.core.util.Resp;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable("productId") int productId) {
        return Resp.ok(productService.findById(productId));
    }

    @PutMapping("/{productId}/decrease")
    public ResponseEntity<?> decreaseQuantity(@PathVariable("productId") int productId, @RequestBody ProductRequest requestDTO) {
        return Resp.ok(productService.decreaseQuantity(productId, requestDTO.quantity(), requestDTO.price()));
    }

    @PutMapping("/{productId}/increase")
    public ResponseEntity<?> increaseQuantity(@PathVariable("productId") int productId, @RequestBody ProductRequest requestDTO) {
        return Resp.ok(productService.increaseQuantity(productId, requestDTO.quantity(), requestDTO.price()));
    }
}