package com.metacoding.order.orders;

import com.metacoding.order.adapter.*;
import com.metacoding.order.adapter.dto.*;
import com.metacoding.order.core.handler.ex.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;
    private final DeliveryClient deliveryClient;

    @Transactional
    public OrderResponse createOrder(int userId, List<OrderRequest.OrderItemDTO> orderItems, String address) {
        // 보상트랜잭션을 위한 변수 선언
        boolean productDecreased = false;
        boolean deliveryCreated = false;
        Order createdOrder = null;

        try {
            // 1. 주문 생성
            createdOrder = orderRepository.save(Order.create(userId));
            final int orderId = createdOrder.getId(); // 스트림에서 사용할 변수

            // 2. 상품 재고 차감
            orderItems.forEach(item ->productClient.decreaseQuantity(
                            new ProductRequest(item.productId(),item.quantity(),item.price())));
            productDecreased = true;

            // 3. 주문 아이템 생성 
            List<OrderItem> createdOrderItems = orderItems.stream()
                .map(item -> OrderItem.create(orderId, item.productId(), item.quantity(), item.price()))
                .toList();
            orderItemRepository.saveAll(createdOrderItems);

            // 4. 배달 생성
            deliveryClient.createDelivery(new DeliveryRequest(orderId, address));
            deliveryCreated = true;

            // 5. 주문 완료
            createdOrder.complete();
            return OrderResponse.from(createdOrder,createdOrderItems);

        } catch (Exception e) {
            // 배달 취소
            if (deliveryCreated) {
                deliveryClient.cancelDelivery(createdOrder.getId());
            }

            // 재고 복구
            if (productDecreased) {
                orderItems.forEach(item -> productClient.increaseQuantity(
                                new ProductRequest(item.productId(), item.quantity(), item.price())
                        ));
            }
            throw new Exception500("주문 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public OrderResponse findById(int orderId) {
        Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception404("주문을 찾을 수 없습니다."));
        List<OrderItem> findOrderItems = orderItemRepository.findByOrderId(orderId)
                .orElseThrow(() -> new Exception404("주문 아이템을 찾을 수 없습니다."));
        return OrderResponse.from(findOrder, findOrderItems);
    }

    @Transactional
    public OrderResponse cancelOrder(int orderId) {
        Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception404("주문을 찾을 수 없습니다."));
        if(findOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new Exception400("주문이 이미 취소되었습니다.");
        }
        List<OrderItem> findOrderItems = orderItemRepository.findByOrderId(orderId)
                .orElseThrow(() -> new Exception404("주문 아이템을 찾을 수 없습니다."));
        // 상품 재고 복구
        findOrderItems.forEach(item -> productClient.increaseQuantity(
                new ProductRequest(item.getProductId(), item.getQuantity(), item.getPrice())
        ));
        // 배달 취소
        deliveryClient.cancelDelivery(orderId);
        // 주문 취소
        findOrder.cancel();
        return OrderResponse.from(findOrder);
    }
}