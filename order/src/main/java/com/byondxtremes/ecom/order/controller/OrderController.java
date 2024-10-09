package com.byondxtremes.ecom.order.controller;

import com.byondxtremes.ecom.order.domain.dto.OrderRequest;
import com.byondxtremes.ecom.order.domain.dto.OrderResponse;
import com.byondxtremes.ecom.order.service.OrderService;
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
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<OrderResponse> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping(value = "/orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderResponse> getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id);
    }
}
