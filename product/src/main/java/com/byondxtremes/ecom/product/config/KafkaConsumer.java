package com.byondxtremes.ecom.product.config;

import com.byondxtremes.ecom.common.kafka.EventsUtil;
import com.byondxtremes.ecom.common.kafka.KafkaMessage;
import com.byondxtremes.ecom.common.utils.JsonUtil;
import com.byondxtremes.ecom.product.domain.entity.ProductEntity;
import com.byondxtremes.ecom.product.repository.ProductRepository;
import com.byondxtremes.ecom.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KafkaConsumer {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaSender kafkaSender;

    @Value("${spring.kafka.producer.ordersTopic}")
    private String ordersTopic;

    @KafkaListener(topics = "${spring.kafka.consumer.productsTopic}", groupId = "${spring.kafka.consumer.groupId}")
    //@KafkaListener(topics = "quickstart-events", groupId = "test-consumer-group")
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group foo: " + message);
        KafkaMessage kafkaMessage = JsonUtil.convertToObject(message, KafkaMessage.class);
        if (EventsUtil.OrderEventsEnum.INIT.name().equals(kafkaMessage.getEvent())) {
            Mono<ProductEntity> productResponseMono =
                productService.getProductEntityById(kafkaMessage.getProductId());
            productResponseMono.doOnSuccess(productResponse -> {
                    if (Integer.valueOf(productResponse.getStock()) > 0) {
                        kafkaSender.sendMessage(KafkaMessage.builder()
                            .event(EventsUtil.ProductEventsEnum.PRODUCT_STOCK_POSTIIVE.name())
                            .orderId(kafkaMessage.getOrderId())
                            .build(), ordersTopic);
                    } else {
                        kafkaSender.sendMessage(KafkaMessage.builder()
                            .event(EventsUtil.ProductEventsEnum.PRODUCT_STOCK_NEAGATIVE.name())
                            .orderId(kafkaMessage.getOrderId())
                            .build(), ordersTopic);
                    }
                })
                .doOnError((e) -> kafkaSender.sendMessage(KafkaMessage.builder()
                    .event(EventsUtil.ProductEventsEnum.PRODUCT_STOCK_NEAGATIVE.name())
                    .orderId(kafkaMessage.getOrderId())
                    .build(), ordersTopic))
                .subscribe();
        } else if (EventsUtil.OrderEventsEnum.UPDATE_PRODUCT_STOCK.name()
            .equals(kafkaMessage.getEvent())) {
            Mono<ProductEntity> productResponseMono =
                productService.getProductEntityById(kafkaMessage.getProductId());
            productResponseMono.doOnSuccess(productEntity -> {
                    productEntity.setStock(
                        String.valueOf(Integer.valueOf(productEntity.getStock()) - 1));
                    productRepository.save(productEntity);
                })
                .doOnError((e) -> System.err.println(
                    "No Product found for product Id: " + kafkaMessage.getProductId()))
                .subscribe();
        }
    }
}
