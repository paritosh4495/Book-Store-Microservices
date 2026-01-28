package com.paritoshpal.orderservice.clients.catalog;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductServiceClient {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceClient.class);

    private final RestClient restClient;

    @Retry(name = "catalog-service", fallbackMethod ="getProductByCodeFallback" )
    public Optional<Product> getProductByCode(String code){
        log.info("Fetching product for code : {} from Catalog Service", code);
         var product = restClient
                 .get()
                 .uri("/api/products/{code}",code)
                 .retrieve()
                 .body(Product.class);
         return Optional.ofNullable(product);
     }

     Optional<Product> getProductByCodeFallback(String code, Throwable t) {
         System.out.println("Fallback executed for getProductByCode with code: " + code);
         log.error("Failed to fetch product for code: {} from Catalog Service. Error: {}", code, t.getMessage());
         return Optional.empty();
     }

}
