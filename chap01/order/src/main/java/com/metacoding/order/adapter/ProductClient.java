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

    // restClient 문법 정리하기!
    // 외부 마이크로서비스 호출
    public void decreaseQuantity(ProductRequest requestDTO) {
        restClient.put()
                .uri("/api/products/{productId}/decrease", requestDTO.productId())
                .body(requestDTO)
                .retrieve()
                .toBodilessEntity();
    }

    // 외부 마이크로서비스 보상트랜잭션 호출
    public void increaseQuantity(ProductRequest requestDTO) {
        restClient.put()
                .uri("/api/products/{productId}/increase", requestDTO.productId())
                .body(requestDTO)
                .retrieve()
                .toBodilessEntity();
    }
}