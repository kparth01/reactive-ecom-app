package com.byondxtremes.ecom.shipping.config;

import com.byondxtremes.ecom.common.kafka.EventsUtil;
import com.byondxtremes.ecom.common.kafka.KafkaMessage;
import com.byondxtremes.ecom.common.utils.JsonUtil;
import com.byondxtremes.ecom.shipping.domain.dto.ShippingRequest;
import com.byondxtremes.ecom.shipping.enums.ShippingStatusEnum;
import com.byondxtremes.ecom.shipping.repository.ShippingRepository;
import com.byondxtremes.ecom.shipping.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KafkaConsumer {

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private KafkaSender kafkaSender;

    @Value("${spring.kafka.producer.ordersTopic}")
    private String ordersTopic;

    @KafkaListener(topics = "${spring.kafka.consumer.shippingTopic}",
        groupId = "${spring.kafka.consumer.groupId}")
    //@KafkaListener(topics = "quickstart-events", groupId = "test-consumer-group")
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group foo: " + message);
        KafkaMessage kafkaMessage = JsonUtil.convertToObject(message, KafkaMessage.class);
        if (EventsUtil.OrderEventsEnum.PREPARE_SHIPPING.name().equals(kafkaMessage.getEvent())) {
            Mono<String> shippingIdMono = shippingService.createShipping(ShippingRequest.builder()
                .status(ShippingStatusEnum.SHIPPING_SUCCESS.name())
                .orderId(kafkaMessage.getOrderId())
                .build());
            shippingIdMono.doOnSuccess(shippingId -> {
                    kafkaSender.sendMessage(KafkaMessage.builder()
                        .event(EventsUtil.ShippingEnum.SHIPPING_SUCCESS.name())
                        .orderId(kafkaMessage.getOrderId())
                        .shippingId(shippingId)
                        .build(), ordersTopic);
                })
                .doOnError((e) -> {
                    kafkaSender.sendMessage(KafkaMessage.builder()
                        .event(EventsUtil.ShippingEnum.SHIPPING_FAILURE.name())
                        .orderId(kafkaMessage.getOrderId())
                        .build(), ordersTopic);
                    System.err.println("Error occurred while arranging shipping for OrderId: " +
                        kafkaMessage.getOrderId());
                })
                .subscribe();
        }

    }
}
