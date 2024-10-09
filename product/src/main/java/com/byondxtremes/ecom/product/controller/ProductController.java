package com.byondxtremes.ecom.product.controller;

import com.byondxtremes.ecom.product.domain.dto.ProductRequest;
import com.byondxtremes.ecom.product.domain.dto.ProductResponse;
import com.byondxtremes.ecom.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ProductResponse> getProducts() {
        return productService.getProducts();
    }

    @GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ProductResponse> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }
}
