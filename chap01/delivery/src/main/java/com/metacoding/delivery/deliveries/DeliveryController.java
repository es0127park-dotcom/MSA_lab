package com.metacoding.delivery.deliveries;

import org.springframework.http.ResponseEntity;
import com.metacoding.delivery.core.util.Resp;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<?> createDelivery(@RequestBody DeliveryRequest requestDTO) {
        return Resp.ok(deliveryService.createDelivery(requestDTO.orderId(), requestDTO.address()));
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<?> getDelivery(@PathVariable("deliveryId") int deliveryId) {
        return Resp.ok(deliveryService.findById(deliveryId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> cancelDelivery(@PathVariable("orderId") int orderId) {
        return Resp.ok(deliveryService.cancelDelivery(orderId));
    }
}