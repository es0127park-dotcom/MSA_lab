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
    private final CreateDeliveryUseCase createDeliveryUseCase;
    private final GetDeliveryUseCase getDeliveryUseCase;
    private final CancelDeliveryUseCase cancelDeliveryUseCase;
    private final CompleteDeliveryUseCase completeDeliveryUseCase;

    @PostMapping
    public ResponseEntity<?> createDelivery(@RequestBody DeliveryRequest requestDTO) {
        return Resp.ok(createDeliveryUseCase.createDelivery(requestDTO.orderId(), requestDTO.address()));
    }

    @GetMapping("/{deliveryId}")
    public ResponseEntity<?> getDelivery(@PathVariable("deliveryId") int deliveryId) {
        return Resp.ok(getDeliveryUseCase.findById(deliveryId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> cancelDelivery(@PathVariable("orderId") int orderId) {
        return Resp.ok(cancelDeliveryUseCase.cancelDelivery(orderId));
    }

    @PutMapping("/{deliveryId}/complete")
    public ResponseEntity<?> completeDelivery(@PathVariable("deliveryId") int deliveryId) {
        return Resp.ok(completeDeliveryUseCase.completeDelivery(deliveryId));
    }
}
