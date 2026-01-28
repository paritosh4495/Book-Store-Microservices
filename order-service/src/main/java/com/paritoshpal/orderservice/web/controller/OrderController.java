package com.paritoshpal.orderservice.web.controller;


import com.paritoshpal.orderservice.domain.OrderService;
import com.paritoshpal.orderservice.domain.SecurityService;
import com.paritoshpal.orderservice.domain.models.CreateOrderRequest;
import com.paritoshpal.orderservice.domain.models.CreateOrderResponse;
import com.paritoshpal.orderservice.domain.models.OrderDTO;
import com.paritoshpal.orderservice.domain.models.OrderSummary;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
class OrderController {

    private final OrderService orderService;
    private final SecurityService securityService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request){
        String userName= securityService.getLoginUserName();
        log.info("Creating order for user: {} ", userName);
        return orderService.createOrder(userName,request);
    }

    @GetMapping
    List<OrderSummary> getOrders(){
        String userName= securityService.getLoginUserName();
        log.info("Fetching orders for user: {} ", userName);
        return orderService.findOrders(userName);
    }

    @GetMapping("/{orderNumber}")
    OrderDTO getOrder(@PathVariable(value = "orderNumber") String orderNumber){
        log.info("Fetching order details for order number: {} ", orderNumber);
        String userName= securityService.getLoginUserName();
        return orderService.findUserOrder(userName, orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with order number: " + orderNumber));

    }

}
