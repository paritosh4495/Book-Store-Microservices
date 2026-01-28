package com.paritoshpal.orderservice.domain.models;

public record OrderSummary(
        String orderNumber,
        OrderStatus status
) {
}
