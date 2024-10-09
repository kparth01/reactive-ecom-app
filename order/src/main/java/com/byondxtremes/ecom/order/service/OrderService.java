package com.byondxtremes.ecom.order.service;


import com.byondxtremes.ecom.common.kafka.EventsUtil;
import com.byondxtremes.ecom.common.kafka.KafkaMessage;
import com.byondxtremes.ecom.common.logging.LogUtility;
import com.byondxtremes.ecom.order.config.KafkaSender;
import com.byondxtremes.ecom.order.domain.dto.OrderRequest;
import com.byondxtremes.ecom.order.domain.dto.OrderResponse;
import com.byondxtremes.ecom.order.domain.entity.OrderEntity;
import com.byondxtremes.ecom.order.mapper.OrderMapper;
import com.byondxtremes.ecom.order.repository.OrderRepository;
import com.byondxtremes.ecom.order.utils.OrderStatusEnum;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

@Service
public class OrderService {

    LogUtility logger = new LogUtility(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private KafkaSender kafkaSender;

    @Value("${spring.kafka.producer.productsTopic}")
    private String productsTopic;

    public Mono<String> createOrder(OrderRequest orderRequest) {
        UUID id = UUID.randomUUID();
        OrderEntity entity = orderMapper.toOrderEntity(orderRequest, id);
        entity.setOrderStatus(OrderStatusEnum.INIT.name());
        orderRepository.save(entity);
        publishOrder(entity, EventsUtil.OrderEventsEnum.INIT.name());
        logger.info("Order saved successfully!!");
        return Mono.just(String.valueOf(id));
    }

    private void publishOrder(OrderEntity entity, String event) {
        KafkaMessage kafkaMessage = KafkaMessage.builder()
            .productId(entity.getProductId())
            .orderId(entity.getId())
            .event(event)
            .build();

        kafkaSender.sendMessage(kafkaMessage, productsTopic);
    }

    public Flux<OrderResponse> getOrders() {
        SdkPublisher<Page<OrderEntity>> response = orderRepository.getAllOrders();
        return Flux.from(response)
            .map(orders -> orders.items().stream())
            .flatMap(orderEntityStream -> Flux.fromStream(orderEntityStream))
            .map(orderEntity -> orderMapper.toOrderResponse(orderEntity));
    }

    public Mono<OrderResponse> getOrderById(String id) {
        //publisher
        PagePublisher<OrderEntity> response = orderRepository.getOrderById(id);
        return Mono.from(response)
            .map(orders -> orders.items().get(0))
            .map(orderEntity -> orderMapper.toOrderResponse(orderEntity));
    }

    public Mono<OrderEntity> getOrderEntityById(String id) {
        PagePublisher<OrderEntity> response = orderRepository.getOrderById(id);
        return Mono.from(response).map(orders -> orders.items().get(0));
    }
}

