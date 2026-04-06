package com.metacoding.order.usecase;

import com.metacoding.order.adapter.producer.OrderEventProducer;
import com.metacoding.order.core.handler.ex.*;
import com.metacoding.order.domain.*;
import com.metacoding.order.adapter.message.*;
import com.metacoding.order.repository.*;
import com.metacoding.order.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderService implements CreateOrderUseCase, GetOrderUseCase, CancelOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderEventProducer orderEventProducer;

    @Override
    @Transactional
    public OrderResponse createOrder(int userId, List<OrderRequest.OrderItemDTO> orderItems, String address) {
        // 1. 주문 생성
        Order createdOrder = orderRepository.save(Order.create(userId));
        final int orderId = createdOrder.getId();

        // 2. 주문 아이템 저장
        List<OrderItem> createdOrderItems = orderItems.stream()
                .map(item -> OrderItem.create(orderId, item.productId(), item.quantity(), item.price()))
                .toList();
        orderItemRepository.saveAll(createdOrderItems);

        // 3. Kafka로 주문 생성 이벤트 발행 
        List<OrderCreatedEvent.OrderItem> messageItems = orderItems.stream()
                .map(item -> new OrderCreatedEvent.OrderItem(item.productId(), item.quantity(), item.price()))
                .toList();

        orderEventProducer.publishOrderCreated(new OrderCreatedEvent(orderId, userId, address, messageItems));

        return OrderResponse.from(createdOrder, createdOrderItems);
    }

    @Transactional
    public void completeOrder(int orderId) {
        Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception404("주문을 찾을 수 없습니다."));
        findOrder.complete();
    }

    @Override
    public OrderResponse findById(int orderId) {
        Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception404("주문을 찾을 수 없습니다."));
        List<OrderItem> findOrderItems = orderItemRepository.findByOrderId(orderId)
                .orElseThrow(() -> new Exception404("주문 아이템을 찾을 수 없습니다."));
        return OrderResponse.from(findOrder, findOrderItems);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(int orderId) {
        Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception404("주문을 찾을 수 없습니다."));
        List<OrderItem> findOrderItems = orderItemRepository.findByOrderId(orderId)
                .orElseThrow(() -> new Exception404("주문 아이템을 찾을 수 없습니다."));
        findOrder.cancel();
        List<OrderCancelledEvent.OrderItem> items = findOrderItems.stream()
                .map(item -> new OrderCancelledEvent.OrderItem(item.getProductId(), item.getQuantity(), item.getPrice()))
                .toList();
        orderEventProducer.publishOrderCancelled(new OrderCancelledEvent(orderId, items));
        return OrderResponse.from(findOrder);
    }
}
