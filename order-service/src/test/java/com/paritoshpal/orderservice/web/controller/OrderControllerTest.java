package com.paritoshpal.orderservice.web.controller;

import com.paritoshpal.orderservice.AbstractIT;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;


class OrderControllerTest extends AbstractIT {

    @Nested
   class CreateOrderTests {
        @Test
        void shouldCreateOrderSuccessfully() {
            mockGetProductByCode("SKU-101", "Wireless Mouse", new BigDecimal("25.99"));
            var payload = """
                        {
                          "items": [
                            {
                              "code": "SKU-101",
                              "name": "Wireless Mouse",
                              "price": 25.99,
                              "quantity": 2
                            }
                          ],
                          "customer": {
                            "name": "John Doe",
                            "email": "john.doe@example.com",
                            "phone": "+44-7700-900123"
                          },
                          "deliveryAddress": {
                            "addressLine1": "221B Baker Street",
                            "addressLine2": "Flat 2",
                            "city": "London",
                            "state": "Greater London",
                            "zipCode": "NW1 6XE",
                            "country": "United Kingdom"
                          }
                        }
                
                
                """;

            var response = given()
                    .contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", org.hamcrest.Matchers.notNullValue());

        }


        @Test
        void shouldReturnBadRequestWhenMandatoryFieldsAreMissing() {
            var payload = """
                {
                  "items": [
                    {
                      "code": "SKU-101",
                      "name": "Wireless Mouse",
                      "price": 25.99,
                      "quantity": 2
                    },
                    {
                      "code": "SKU-202",
                      "name": "Mechanical Keyboard",
                      "price": 89.50,
                      "quantity": 1
                    }
                  ],
                  "customer": {
                    "name": "John Doe",
                    "email": "john.doe@example.com",
                    "phone": "+44-7700-900123"
                  },
                  "deliveryAddress": {
                    "addressLine1": "221B Baker Street",
                    "addressLine2": "Flat 2",
                    "city": "London",
                    "state": "Greater London",
                    "zipCode": "NW1 6XE",
                    "country": ""
                  }
                }
                
                """;
            given()
                    .contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

    }
}