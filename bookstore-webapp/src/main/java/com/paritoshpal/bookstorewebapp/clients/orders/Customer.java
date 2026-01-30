package com.paritoshpal.bookstorewebapp.clients.orders;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Customer(
        @NotBlank(message = "Customer Name is required")
        String name,
        @NotBlank(message = "Customer Email is required")
        @Email(message = "Invalid email format")
        String email,
        @NotBlank(message = "Customer Phone is required")
        String phone
) {
}
