package com.paritoshpal.notificationservice.domain.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItem(
        @NotBlank(message = "Code is required")
        String code,
        @NotBlank(message = "Name is required")
        String name,
        @NotNull(message = "Price is required")
        BigDecimal price,
        @NotNull
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {
}
