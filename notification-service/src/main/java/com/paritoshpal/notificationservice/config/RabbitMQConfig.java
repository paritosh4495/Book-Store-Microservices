package com.paritoshpal.notificationservice.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.paritoshpal.notificationservice.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final ApplicationProperties properties;


    // Define DirectExchange bean
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(properties.orderEventsExchange());
    }

    // Define a Queue bean

    @Bean
    Queue newOrdersQueue(){
        return QueueBuilder.durable(properties.newOrdersQueue()).build();
    }

    // Bind the Queue to the Exchange with a routing key

    @Bean
    Binding newOrdersQueueBinding(){
        return BindingBuilder.bind(newOrdersQueue()).to(exchange()).with(properties.newOrdersQueue());
    }

    // Define another Queue bean

    @Bean
    Queue deliveredOrdersQueue(){
        return QueueBuilder.durable(properties.deliveredOrdersQueue()).build();
    }

    // Bind the second Queue to the Exchange with a routing key

    @Bean
    Binding deliveredOrdersQueueBinding(){
        return BindingBuilder.bind(deliveredOrdersQueue()).to(exchange()).with(properties.deliveredOrdersQueue());
    }

    // Define Cancelled Orders Queue bean

    @Bean
    Queue cancelledOrdersQueue(){
        return QueueBuilder.durable(properties.cancelledOrdersQueue()).build();
    }

    // Bind the Cancelled Orders Queue to the Exchange with a routing key

    @Bean
    Binding cancelledOrdersQueueBinding(){
        return BindingBuilder.bind(cancelledOrdersQueue()).to(exchange()).with(properties.cancelledOrdersQueue());
    }

    // Add error Orders Queue bean

    @Bean
    Queue errorOrdersQueue(){
        return QueueBuilder.durable(properties.errorOrdersQueue()).build();
    }

    // Bind the error Orders Queue to the Exchange with a routing key

    @Bean
    Binding errorOrdersQueueBinding() {
        return BindingBuilder.bind(errorOrdersQueue()).to(exchange()).with(properties.errorOrdersQueue());
    }

    // Template for sending messages to RabbitMQ

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter(objectMapper));
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jacksonConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }


}
