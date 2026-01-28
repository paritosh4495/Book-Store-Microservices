package com.paritoshpal.orderservice.domain;

import com.paritoshpal.orderservice.domain.models.CreateOrderRequest;
import com.paritoshpal.orderservice.domain.models.CreateOrderResponse;
import com.paritoshpal.orderservice.domain.models.OrderCreatedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public CreateOrderResponse createOrder(String userName,  CreateOrderRequest request) {
        orderValidator.validate(request);
        OrderEntity orderEntity = OrderMapper.convertToEntity(request);
        orderEntity.setUserName(userName);
        OrderEntity savedOrder = orderRepository.save(orderEntity);
        log.info("Order created with order number: {} for user: {}", savedOrder.getOrderNumber(), userName);
        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(savedOrder);
        orderEventService.save(orderCreatedEvent);
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }
}
