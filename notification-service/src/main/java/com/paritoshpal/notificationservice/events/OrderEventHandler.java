package com.paritoshpal.notificationservice.events;

import com.paritoshpal.notificationservice.domain.NotificationService;
import com.paritoshpal.notificationservice.domain.OrderEventEntity;
import com.paritoshpal.notificationservice.domain.OrderEventRepository;
import com.paritoshpal.notificationservice.domain.models.OrderCancelledEvent;
import com.paritoshpal.notificationservice.domain.models.OrderCreatedEvent;
import com.paritoshpal.notificationservice.domain.models.OrderDeliveredEvent;
import com.paritoshpal.notificationservice.domain.models.OrderErrorEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class OrderEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final NotificationService notificationService;
    private final OrderEventRepository orderEventRepository;

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event) {
        // Handle the order created event (e.g., send notification)
        log.info("Handling Order Created Event for Order ID: {}", event);
        if(orderEventRepository.existsByEventId(event.eventId())){
            log.warn("Duplicate Order Created Event received with Event ID: {}", event.eventId());
            return;
        }
        notificationService.sendOrderCreatedNotification(event);
        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEventEntity);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        // Handle the order delivered event (e.g., send notification)
        log.info("Handling Order Delivered Event: {}", event);
        if(orderEventRepository.existsByEventId(event.eventId())){
            log.warn("Duplicate Order Delivered Event received with Event ID: {}", event.eventId());
            return;
        }
        notificationService.sendOrderDeliveredNotification(event);
        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEventEntity);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event) {
        // Handle the order cancelled event (e.g., send notification)
        log.info("Handling Order Cancelled Event: {}", event);
        if(orderEventRepository.existsByEventId(event.eventId())){
            log.warn("Duplicate Order Cancelled Event received with Event ID: {}", event.eventId());
            return;
        }
        notificationService.sendOrderCancelledNotification(event);
        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEventEntity);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleOrderErrorEvent(OrderErrorEvent event) {
        // Handle the order error event (e.g., send notification)
        log.info("Handling Order Error Event: {}", event);
        if(orderEventRepository.existsByEventId(event.eventId())){
            log.warn("Duplicate Order Error Event received with Event ID: {}", event.eventId());
            return;
        }
        notificationService.sendOrderErrorEventNotification(event);
        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEventEntity);
    }


}
