package com.paritoshpal.bookstorewebapp.clients.orders;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;


public interface OrderServiceClient {

    @PostExchange("/orders/api/orders")
    OrderConfirmationDTO createOrder(
            @RequestBody  CreateOrderRequest request);

    @GetExchange("/orders/api/orders")
    List<OrderSummary> getOrders(
    );


    @GetExchange("/orders/api/orders/{orderNumber}")
    OrderDTO getOrder(
            @PathVariable String orderNumber);


}


// A comment