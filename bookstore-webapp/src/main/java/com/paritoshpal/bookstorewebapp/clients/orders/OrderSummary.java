package com.paritoshpal.bookstorewebapp.clients.orders;


public record OrderSummary(
        String orderNumber,
        OrderStatus status
) {
}
