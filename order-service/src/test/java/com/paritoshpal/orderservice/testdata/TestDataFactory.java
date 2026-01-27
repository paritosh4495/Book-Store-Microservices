package com.paritoshpal.orderservice.testdata;

import com.paritoshpal.orderservice.domain.models.Address;
import com.paritoshpal.orderservice.domain.models.CreateOrderRequest;
import com.paritoshpal.orderservice.domain.models.Customer;
import com.paritoshpal.orderservice.domain.models.OrderItem;
import org.instancio.Instancio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.instancio.Select.field;


public class TestDataFactory {

    static final List<String> VALID_COUNTRIES =
            List.of("USA", "Canada", "UK", "Germany", "France", "Australia");
    static final Set<OrderItem> VALID_ORDER_ITEMS =
            Set.of(new OrderItem("P100","Product 1",new BigDecimal("25.50"),1));
    static final Set<OrderItem> INVALID_ORDER_ITEMS =
            Set.of(new OrderItem("ABCD","Product 1",new BigDecimal("25.50"),1));
/*


set(...): Use this when you want a HARDCODED, EXACT value.

Example: "I want the country to be exactly empty string "" to test an error."

Example: "I want the items list to be exactly this specific VALID_ORDER_ITEMS set."

generate(...): Use this when you want RANDOM DATA but following a RULE.

Example: "I don't care what the email is, as long as it looks like an email."

If you just let Instancio default, it might generate "Xy7z#9L", which isn't a valid email. Your validation @Email would fail the test before you even start.

So you say: gen.text().pattern("#a#a#a@mail.com"). It might generate abc@mail.com or xyz@mail.com. It's still random, but "Valid Random".
 */


    public static CreateOrderRequest createValidOrderRequest(){
        return Instancio.of(CreateOrderRequest.class)
                // 1. GENERATE: "Find the Customer's email field. Fill it with text matching this pattern."
                // We do this so the @Email validation passes.
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))

                // 2. SET: "Find the 'items' field. Force it to be our hardcoded VALID_ORDER_ITEMS list."
                // We do this because we want predictable product prices/codes for our tests.
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)

                // 3. GENERATE: "Find the Address country. Pick ONE random value from our allowed list (USA, UK, etc)."
                // We do this to ensure we don't get a random string like "Mars" which might be invalid logic-wise.
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))

                // 4. CREATE: Build the object. Everything else (Phone, City, Zip) is auto-filled with random strings.
                .create();
    }


    public static CreateOrderRequest createOrderRequestsWithInvalidCustomer(){
        return Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email),gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .set(field(Customer::phone),"")
                .generate(field(Address::country),gen->gen.oneOf(VALID_COUNTRIES))
                .set(field(CreateOrderRequest::items),VALID_ORDER_ITEMS)
                .create();
    }

    public static CreateOrderRequest createOrderRequestWithInvalidDeliveryAddress() {
        return Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
                .set(field(Address::country),"")
                .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
                .create();

    }

    public static CreateOrderRequest createOrderRequestWithNoItems() {
        return Instancio.of(CreateOrderRequest.class)
                .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@mail.com"))
                .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTRIES))
                .set(field(CreateOrderRequest::items), Set.of())
                .create();
    }



    }
