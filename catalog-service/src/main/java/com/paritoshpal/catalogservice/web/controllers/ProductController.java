package com.paritoshpal.catalogservice.web.controllers;

import com.paritoshpal.catalogservice.domain.PageResult;
import com.paritoshpal.catalogservice.domain.Product;
import com.paritoshpal.catalogservice.domain.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
class ProductController {

    private final ProductService productService;

    @GetMapping
    PageResult<Product> getProducts(@RequestParam(name = "page" , defaultValue = "1") int pageNo) {
        // Logic to fetch and return products
        return productService.getProducts(pageNo);
    }
}
