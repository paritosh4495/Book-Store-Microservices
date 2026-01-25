package com.paritoshpal.catalogservice.web.controllers;

import com.paritoshpal.catalogservice.domain.PageResult;
import com.paritoshpal.catalogservice.domain.Product;
import com.paritoshpal.catalogservice.domain.ProductNotFoundException;
import com.paritoshpal.catalogservice.domain.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
class ProductController {

    private final ProductService productService;

    @GetMapping
    PageResult<Product> getProducts(@RequestParam(name = "page" , defaultValue = "1") int pageNo) {
        // Logic to fetch and return products
        return productService.getProducts(pageNo);
    }

    @GetMapping("/{code}")
    ResponseEntity<Product> getProductByCode(@PathVariable String code) {
        return productService.getProductByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> ProductNotFoundException.forCode(code));
    }

}
