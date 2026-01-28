package com.paritoshpal.orderservice.domain;

import com.paritoshpal.orderservice.domain.models.OrderCreatedEvent;

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
                orderEntity.getCreatedAt()
        );
    }
}
