package com.paritoshpal.bookstorewebapp.clients.orders;

public record OrderConfirmationDTO(
        String orderNumber,
        OrderStatus status
) {
}
