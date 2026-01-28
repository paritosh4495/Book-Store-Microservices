package com.paritoshpal.orderservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paritoshpal.orderservice.domain.models.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderEventService {

    private static final Logger log = LoggerFactory.getLogger(OrderEventService.class);

    private final OrderEventRepository orderEventRepository;
    private final ObjectMapper objectMapper;
    private final OrderEventPublisher orderEventPublisher;


    void save(OrderCreatedEvent event){
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CREATED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJasonPayload(event));
        log.info("Saving OrderCreatedEvent with eventId: {} for orderNumber: {}", event.eventId(), event.orderNumber());
        orderEventRepository.save(orderEvent);
        log.info("OrderCreatedEvent Saved Successfully");
    }

    void save(OrderDeliveredEvent event){
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_DELIVERED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJasonPayload(event));
        log.info("Saving OrderDeliveredEvent with eventId: {} for orderNumber: {}", event.eventId(), event.orderNumber());
        orderEventRepository.save(orderEvent);
        log.info("OrderDeliveredEvent Saved Successfully");
    }

    void save(OrderCancelledEvent event){
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CANCELED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJasonPayload(event));
        log.info("Saving OrderCancelledEvent with eventId: {} for orderNumber: {}", event.eventId(), event.orderNumber());
        orderEventRepository.save(orderEvent);
        log.info("OrderCancelledEvent Saved Successfully");
    }

    void save(OrderErrorEvent event){
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_PROCESSING_FAILED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJasonPayload(event));
        log.info("Saving OrderErrorEvent with eventId: {} for orderNumber: {}", event.eventId(), event.orderNumber());
        orderEventRepository.save(orderEvent);
        log.info("OrderErrorEvent Saved Successfully");
    }



    public void publishOrderEvents(){
        // 1. To Fetch all the unprocessed order events from the database Sort it by createdAt
        Sort sort = Sort.by("createdAt").ascending();
        List<OrderEventEntity> events = orderEventRepository.findAll(sort);
        // 2. Publish each event to the message broker (e.g., , RabbitMQ)
        events.forEach(event -> {
            log.info(" -> -> Sent event for publishing with eventId: {} for orderNumber: {}", event.getEventId(), event.getOrderNumber());
            this.publishEvent(event);
            log.info("Published event Successfully");
            // 3. After successful publishing, delete the event from the database

            orderEventRepository.delete(event);
            log.info("Deleted published event with eventId: {} from database", event.getEventId());
        });
    }

    private void publishEvent(OrderEventEntity event){
        // Now Publish Each Incoming Event to Message Broker RabbitMQ
        OrderEventType eventType = event.getEventType();
        switch (eventType){
            case ORDER_CREATED:
                OrderCreatedEvent orderCreatedEvent = fromJsonPayload(event.getPayload(), OrderCreatedEvent.class);
                // Publish to RabbitMQ
                orderEventPublisher.publish(orderCreatedEvent);
                break;
            case ORDER_DELIVERED:
                OrderDeliveredEvent orderDeliveredEvent = fromJsonPayload(event.getPayload(), OrderDeliveredEvent.class);
                // Publish to RabbitMQ
                orderEventPublisher.publish(orderDeliveredEvent);
                break;
            case ORDER_CANCELED:
                OrderCancelledEvent orderCancelledEvent = fromJsonPayload(event.getPayload(), OrderCancelledEvent.class);
                // Publish to RabbitMQ
                orderEventPublisher.publish(orderCancelledEvent);
                break;
            case ORDER_PROCESSING_FAILED:
                OrderErrorEvent orderErrorEvent = fromJsonPayload(event.getPayload(), OrderErrorEvent.class);
                // Publish to RabbitMQ
                orderEventPublisher.publish(orderErrorEvent);
                break;
            default:
                log.warn("Unsupported OrderEventType: {}", eventType);
        }
    }


    private String toJasonPayload(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJsonPayload(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
