package com.paritoshpal.orderservice.domain;

import com.paritoshpal.orderservice.domain.models.OrderCancelledEvent;
import com.paritoshpal.orderservice.domain.models.OrderCreatedEvent;
import com.paritoshpal.orderservice.domain.models.OrderDeliveredEvent;
import com.paritoshpal.orderservice.domain.models.OrderErrorEvent;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderEventMapper {

    static OrderCreatedEvent buildOrderCreatedEvent(OrderEntity orderEntity){
        return new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                orderEntity.getItems().stream().map(OrderMapper::fromOrderItemEntity).collect(Collectors.toSet()),
                orderEntity.getCustomer(),
                orderEntity.getDeliveryAddress(),
                LocalDateTime.now()
        );
    }

    static OrderDeliveredEvent buildOrderDeliveredEvent(OrderEntity orderEntity){
        return new OrderDeliveredEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                orderEntity.getItems().stream().map(OrderMapper::fromOrderItemEntity).collect(Collectors.toSet()),
                orderEntity.getCustomer(),
                orderEntity.getDeliveryAddress(),
                LocalDateTime.now()
        );
    }

    static OrderCancelledEvent buildOrderCancelledEvent(OrderEntity orderEntity, String reason){
        return new OrderCancelledEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                orderEntity.getItems().stream().map(OrderMapper::fromOrderItemEntity).collect(Collectors.toSet()),
                orderEntity.getCustomer(),
                orderEntity.getDeliveryAddress(),
                reason,
                LocalDateTime.now()
        );
    }

    static OrderErrorEvent buildOrderErrorEvent(OrderEntity orderEntity, String reason){
        return new OrderErrorEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                orderEntity.getItems().stream().map(OrderMapper::fromOrderItemEntity).collect(Collectors.toSet()),
                orderEntity.getCustomer(),
                orderEntity.getDeliveryAddress(),
                reason,
                LocalDateTime.now()
        );
    }
}
