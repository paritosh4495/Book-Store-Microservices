package com.paritoshpal.orderservice.web.controller;

import com.paritoshpal.orderservice.AbstractIT;
import com.paritoshpal.orderservice.domain.models.OrderSummary;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Sql("/test-orders.sql")
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

    @Nested
    class GetOrdersTests {

        @Test
        void shouldGetOrdersSuccessfully(){
            List<OrderSummary> orderSummaries = given()
                    .when()
                    .get("/api/orders")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .body()
                    .as(new TypeRef<>() {});

            assertEquals(2, orderSummaries.size());
        }
    }

    @Nested
    class GetOrderByOrderNumberTests {
        String orderNumber = "order-123";

        @Test
        void shouldGetOrderSuccessfully() {
            given().when()
                    .get("/api/orders/{orderNumber}", orderNumber)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("orderNumber", org.hamcrest.Matchers.equalTo(orderNumber))
                    .body("items.size()", org.hamcrest.Matchers.equalTo(2));
        }
    }
}