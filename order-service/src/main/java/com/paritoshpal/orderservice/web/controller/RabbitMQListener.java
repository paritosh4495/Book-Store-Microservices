package com.paritoshpal.orderservice.web.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListener {

//    @RabbitListener(queues = "${orders.new-orders-queue}")
//    public void handleNewOrderEvent(MyPayload payload) {
//        System.out.println("Received new order event with content: " + payload.content());
//    }
//
//    @RabbitListener(queues = "${orders.delivered-orders-queue}")
//    public void handleDeliveredOrderEvent(MyPayload payload) {
//        System.out.println("Received delivered order event with content: " + payload.content());
//    }



}
