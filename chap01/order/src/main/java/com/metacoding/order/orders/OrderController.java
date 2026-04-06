package com.metacoding.order.orders;

import org.springframework.http.ResponseEntity;
import com.metacoding.order.core.util.Resp;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest requestDTO, @RequestAttribute("userId") Integer userId) {
        return Resp.ok(orderService.createOrder(userId, requestDTO.orderItems(), requestDTO.address()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable("orderId") int orderId) {
        return Resp.ok(orderService.findById(orderId));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderId") int orderId) {
        return Resp.ok(orderService.cancelOrder(orderId));
    }
}