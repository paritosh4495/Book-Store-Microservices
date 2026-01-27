package com.paritoshpal.orderservice.web.controller;


import com.paritoshpal.orderservice.domain.OrderService;
import com.paritoshpal.orderservice.domain.SecurityService;
import com.paritoshpal.orderservice.domain.models.CreateOrderRequest;
import com.paritoshpal.orderservice.domain.models.CreateOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

}
