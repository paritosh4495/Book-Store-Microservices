package com.paritoshpal.orderservice.domain;

import com.paritoshpal.orderservice.domain.models.OrderStatus;
import com.paritoshpal.orderservice.domain.models.OrderSummary;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
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


    @Query("""
select new com.paritoshpal.orderservice.domain.models.OrderSummary(o.orderNumber, o.orderStatus)
from OrderEntity o where o.userName = :userName
""")
    List<OrderSummary> findByUserName(String userName);

    @Query("""
    select distinct o 
    from OrderEntity o left join fetch o.items
    where o.userName = :userName and o.orderNumber = :orderNumber
""")
    Optional<OrderEntity> findByUserNameAndOrderNumber(String userName, String orderNumber);
}
