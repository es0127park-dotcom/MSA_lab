package com.metacoding.delivery.web;

import com.metacoding.delivery.usecase.*;
import com.metacoding.delivery.web.dto.DeliveryRequest;
import org.springframework.http.ResponseEntity;
import com.metacoding.delivery.core.util.Resp;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {
    private final SaveDeliveryUseCase saveDeliveryUseCase;
    private final GetDeliveryUseCase getDeliveryUseCase;
    private final CancelDeliveryUseCase cancelDeliveryUseCase;

    @PostMapping
    public ResponseEntity<?> createDelivery(@RequestBody DeliveryRequest requestDTO) {
        return Resp.ok(saveDeliveryUseCase.saveDelivery(requestDTO.orderId(), requestDTO.address()));
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<?> getDelivery(@PathVariable("deliveryId") int deliveryId) {
        return Resp.ok(getDeliveryUseCase.findById(deliveryId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> cancelDelivery(@PathVariable("orderId") int orderId) {
        return Resp.ok(cancelDeliveryUseCase.cancelDelivery(orderId));
    }
}
