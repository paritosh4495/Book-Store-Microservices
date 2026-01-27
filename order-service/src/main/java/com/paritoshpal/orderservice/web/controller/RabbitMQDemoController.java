package com.paritoshpal.orderservice.web.controller;

import com.paritoshpal.orderservice.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rabbit")
@RequiredArgsConstructor
public class RabbitMQDemoController {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;


    @PostMapping("/send")
    public void sendMessage(@RequestBody MyMessage message){
        rabbitTemplate.convertAndSend(
                properties.orderEventsExchange(),
                message.routingKey(),
                message.payload()
        );
    }


}

record MyMessage(String routingKey, MyPayload payload){

}

record MyPayload(String content){

}
