package com.byondxtremes.ecom.shipping.controller;

import com.byondxtremes.ecom.shipping.domain.dto.ShippingRequest;
import com.byondxtremes.ecom.shipping.domain.dto.ShippingResponse;
import com.byondxtremes.ecom.shipping.service.ShippingService;
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
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    @PostMapping(value = "/shipping", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> createShipping(@RequestBody ShippingRequest productRequest) {
        return shippingService.createShipping(productRequest);
    }

    @GetMapping(value = "/shipping", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ShippingResponse> getShippingDetails() {
        return shippingService.getShipping();
    }

    @GetMapping(value = "/shipping/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ShippingResponse> getShippingById(@PathVariable String id) {
        return shippingService.getShippingById(id);
    }
}
