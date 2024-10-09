package com.byondxtremes.ecom.shipping.mapper;

import com.byondxtremes.ecom.shipping.domain.dto.ShippingRequest;
import com.byondxtremes.ecom.shipping.domain.dto.ShippingResponse;
import com.byondxtremes.ecom.shipping.domain.entity.ShippingEntity;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring"
)
public interface ShippingMapper {

    default ShippingEntity toShippingEntity(ShippingRequest shippingRequest, UUID id) {
        return ShippingEntity.builder()
            .id(String.valueOf(id))
            .orderId(shippingRequest.getOrderId())
            .shippingDate(shippingRequest.getShippingDate())
            .status(shippingRequest.getStatus())
            .build();
    }

    default ShippingResponse toShippingResponse(ShippingEntity shippingEntity) {
        ShippingResponse pe = ShippingResponse.builder()
            .id(shippingEntity.getId())
            .orderId(shippingEntity.getOrderId())
            .shippingDate(shippingEntity.getShippingDate())
            .status(shippingEntity.getStatus())
            .build();
        return pe;
    }
}
