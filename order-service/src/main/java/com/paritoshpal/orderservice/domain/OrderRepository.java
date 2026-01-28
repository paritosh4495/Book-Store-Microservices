package com.paritoshpal.orderservice.domain;

import com.paritoshpal.orderservice.domain.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByOrderStatus(OrderStatus orderStatus);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default  void updateOrderStatus(String orderNumber, OrderStatus orderStatus) {
        OrderEntity order = this.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with order number: " + orderNumber));
        order.setOrderStatus(orderStatus);
        this.save(order);
    }


}
