package com.paritoshpal.notificationservice.events;

import com.paritoshpal.notificationservice.domain.models.OrderCancelledEvent;
import com.paritoshpal.notificationservice.domain.models.OrderCreatedEvent;
import com.paritoshpal.notificationservice.domain.models.OrderDeliveredEvent;
import com.paritoshpal.notificationservice.domain.models.OrderErrorEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
class OrderEventHandler {

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event) {
        // Handle the order created event (e.g., send notification)
        System.out.println("Handling Order Created Event for Order ID: " + event);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        // Handle the order delivered event (e.g., send notification)
        System.out.println("Handling Order Delivered Event: " + event);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event) {
        // Handle the order cancelled event (e.g., send notification)
        System.out.println("Handling Order Cancelled Event: " + event);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleOrderErrorEvent(OrderErrorEvent event) {
        // Handle the order error event (e.g., send notification)
        System.out.println("Handling Order Error Event: " + event);
    }


}
