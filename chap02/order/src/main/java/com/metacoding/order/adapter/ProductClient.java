package com.metacoding.order.adapter;

import com.metacoding.order.adapter.dto.ProductRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://product-service:8082")
                .build();
    }

    public void decreaseQuantity(ProductRequest requestDTO) {
        restClient.put()
                .uri("/api/products/{productId}/decrease", requestDTO.productId())
                .body(requestDTO)
                .retrieve()
                .toBodilessEntity();
    }

    public void increaseQuantity(ProductRequest requestDTO) {
        restClient.put()
                .uri("/api/products/{productId}/increase", requestDTO.productId())
                .body(requestDTO)
                .retrieve()
                .toBodilessEntity();
    }
}