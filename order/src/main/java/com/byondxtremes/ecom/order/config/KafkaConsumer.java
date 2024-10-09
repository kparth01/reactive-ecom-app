package com.byondxtremes.ecom.order.config;

import com.byondxtremes.ecom.common.kafka.EventsUtil;
import com.byondxtremes.ecom.common.kafka.KafkaMessage;
import com.byondxtremes.ecom.common.utils.JsonUtil;
import com.byondxtremes.ecom.order.domain.entity.OrderEntity;
import com.byondxtremes.ecom.order.mapper.OrderMapper;
import com.byondxtremes.ecom.order.repository.OrderRepository;
import com.byondxtremes.ecom.order.service.OrderService;
import com.byondxtremes.ecom.order.utils.OrderStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KafkaConsumer {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaSender kafkaSender;

    @Autowired
    private OrderMapper orderMapper;

    @Value("${spring.kafka.producer.productsTopic}")
    private String productsTopic;

    @Value("${spring.kafka.producer.shippingTopic}")
    private String shippingTopic;

    @KafkaListener(topics = {"${spring.kafka.consumer.ordersTopic}"},
        groupId = "${spring.kafka.consumer.groupId}")
    //@KafkaListener(topics = "quickstart-events", groupId = "test-consumer-group")
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group foo: " + message);
        KafkaMessage kafkaMessage = JsonUtil.convertToObject(message, KafkaMessage.class);
        if (EventsUtil.ProductEventsEnum.PRODUCT_STOCK_POSTIIVE.name()
            .equals(kafkaMessage.getEvent())) {
            Mono<OrderEntity> orderEntityMono =
                orderService.getOrderEntityById(kafkaMessage.getOrderId());
            orderEntityMono.map(orderEntity -> {
                    orderEntity.setOrderStatus(OrderStatusEnum.CREATED.name());
                    kafkaSender.sendMessage(KafkaMessage.builder()
                        .orderId(orderEntity.getId())
                        .event(EventsUtil.OrderEventsEnum.PREPARE_SHIPPING.name())
                        .build(), shippingTopic);
                    return orderEntity;
                })
                .map(orderRepository::save)
                .subscribe();
        } else if (EventsUtil.ProductEventsEnum.PRODUCT_STOCK_NEAGATIVE.name()
            .equals(kafkaMessage.getEvent())) {
            Mono<OrderEntity> orderEntityMono =
                orderService.getOrderEntityById(kafkaMessage.getOrderId());
            orderEntityMono.map(orderEntity -> {
                    orderEntity.setOrderStatus(OrderStatusEnum.ORDER_REVERTED.name());
                    return orderEntity;
                })
                .map(orderRepository::save)
                .subscribe();
        } else if (EventsUtil.ShippingEnum.SHIPPING_SUCCESS.name()
            .equals(kafkaMessage.getEvent())) {
            Mono<OrderEntity> orderEntityMono =
                orderService.getOrderEntityById(kafkaMessage.getOrderId());
            orderEntityMono.map(orderEntity -> {
                    orderEntity.setOrderStatus(OrderStatusEnum.SHIPPED.name());
                    kafkaSender.sendMessage(KafkaMessage.builder()
                        .productId(orderEntity.getProductId())
                        .event(EventsUtil.OrderEventsEnum.UPDATE_PRODUCT_STOCK.name())
                        .build(), productsTopic);
                    return orderEntity;
                })
                .map(orderRepository::save)
                .subscribe();
        } else if (EventsUtil.ShippingEnum.SHIPPING_FAILURE.name()
            .equals(kafkaMessage.getEvent())) {
            Mono<OrderEntity> orderEntityMono =
                orderService.getOrderEntityById(kafkaMessage.getOrderId());
            orderEntityMono.map(orderEntity -> {
                    orderEntity.setOrderStatus(OrderStatusEnum.SHIPPING_FAILED.name());
                    return orderEntity;
                })
                .map(orderRepository::save)
                .subscribe();
        }
    }
}