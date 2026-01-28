package com.paritoshpal.orderservice.domain;

import com.paritoshpal.orderservice.domain.models.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("INDIA", "USA", "UK", "GERMANY");

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

    public void processNewOrders() {
        // 1. Fetch all order entities with status NEW
        List<OrderEntity> orders = orderRepository.findByOrderStatus(OrderStatus.NEW);
        log.info("Found {} new orders to process", orders.size());
        // 2. Process each order (business logic can be added here)
        for (OrderEntity order : orders) {
            this.process(order);
        }

    }

    private void process(OrderEntity order) {
      try {
          if(canBeDelivered(order)) {
              orderRepository.updateOrderStatus(order.getOrderNumber(),OrderStatus.DELIVERED);
              log.info("Order number: {} can be delivered. Status updated to DELIVERED", order.getOrderNumber());
              orderEventService.save(OrderEventMapper.buildOrderDeliveredEvent(order));
          } else {
              orderRepository.updateOrderStatus(order.getOrderNumber(),OrderStatus.CANCELLED);
              log.info("Order number: {} cannot be delivered to country: {}. Status updated to CANCELLED",
                      order.getOrderNumber(), order.getDeliveryAddress().country());
              orderEventService.save(OrderEventMapper.buildOrderCancelledEvent(order,"Cant Deliver to the specified country"));
          }
      } catch (RuntimeException e){
            log.error("Error processing order number: {}. Error: {}", order.getOrderNumber(), e.getMessage());
            orderRepository.updateOrderStatus(order.getOrderNumber(),OrderStatus.ERROR);
            orderEventService.save(OrderEventMapper.buildOrderErrorEvent(order, e.getMessage()));
      }
    }

    private boolean canBeDelivered(OrderEntity order) {
        String country = order.getDeliveryAddress().country();
        return DELIVERY_ALLOWED_COUNTRIES.contains(country.toUpperCase());
    }

    public List<OrderSummary> findOrders(String userName) {
       return orderRepository.findByUserName(userName);
    }


    public Optional<OrderDTO> findUserOrder(String userName, String orderNumber) {
        return orderRepository.findByUserNameAndOrderNumber(userName, orderNumber).
                map(OrderMapper::convertToDTO);
    }
}
