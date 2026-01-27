package com.paritoshpal.orderservice.domain;

import com.paritoshpal.orderservice.clients.catalog.Product;
import com.paritoshpal.orderservice.clients.catalog.ProductServiceClient;
import com.paritoshpal.orderservice.domain.models.CreateOrderRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderValidator {

    private static final Logger logger = LoggerFactory.getLogger(OrderValidator.class);

    private final ProductServiceClient client;

     void validate(CreateOrderRequest request) {
         // For each order item inside the order request
         // get the product code and call the catalog service to check if the product exists
         request.items().forEach(item -> {
             Product product = client.getProductByCode(item.code()).orElseThrow(()-> new InvalidOrderException("Invalid Product Code: " + item.code()));
             if(item.price().compareTo(product.price()) != 0){
                 logger.error("Price mismatch for product code: {}. Expected: {}, Found: {}", item.code(), product.price(), item.price());
                 throw new InvalidOrderException("Price mismatch for product code: " + item.code());
             }
         });
    }
}
