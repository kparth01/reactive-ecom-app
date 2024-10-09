package com.byondxtremes.ecom.shipping.service;


import com.byondxtremes.ecom.common.logging.LogUtility;
import com.byondxtremes.ecom.shipping.domain.dto.ShippingRequest;
import com.byondxtremes.ecom.shipping.domain.dto.ShippingResponse;
import com.byondxtremes.ecom.shipping.domain.entity.ShippingEntity;
import com.byondxtremes.ecom.shipping.mapper.ShippingMapper;
import com.byondxtremes.ecom.shipping.repository.ShippingRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

@Service
public class ShippingService {

    LogUtility logger = new LogUtility(ShippingService.class);

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private ShippingMapper shippingMapper;

    public Mono<String> createShipping(ShippingRequest shippingRequest) {
        UUID id = UUID.randomUUID();
        ShippingEntity entity = shippingMapper.toShippingEntity(shippingRequest, id);
        shippingRepository.save(entity);
        logger.info("Shipping saved successfully!!");
        return Mono.just(String.valueOf(id));
    }

    public Flux<ShippingResponse> getShipping() {
        SdkPublisher<Page<ShippingEntity>> response = shippingRepository.getAllShipping();
        return Flux.from(response)
            .map(products -> products.items().stream())
            .flatMap(productEntityStream -> Flux.fromStream(productEntityStream))
            .map(shippingEntity -> shippingMapper.toShippingResponse(shippingEntity));
    }

    public Mono<ShippingResponse> getShippingById(String id) {
        //publisher
        PagePublisher<ShippingEntity> response = shippingRepository.getShippingById(id);
        return Mono.from(response)
            .map(productEntityPage -> productEntityPage.items().get(0))
            .map(shippingEntity -> shippingMapper.toShippingResponse(shippingEntity));
    }

    public Mono<ShippingEntity> getShippingEntityById(String id) {
        PagePublisher<ShippingEntity> response = shippingRepository.getShippingById(id);
        return Mono.from(response).map(shippingEntities -> shippingEntities.items().get(0));
    }
}

