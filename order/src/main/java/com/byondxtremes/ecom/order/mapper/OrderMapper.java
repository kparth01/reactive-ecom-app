package com.byondxtremes.ecom.order.mapper;

import com.byondxtremes.ecom.order.domain.dto.OrderRequest;
import com.byondxtremes.ecom.order.domain.dto.OrderResponse;
import com.byondxtremes.ecom.order.domain.entity.OrderEntity;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring"
)
public interface OrderMapper {

    default OrderEntity toOrderEntity(OrderRequest orderRequest, UUID id) {
        return OrderEntity.builder()
            .id(String.valueOf(id))
            .productId(orderRequest.getProductId())
            .name(orderRequest.getName())
            .price(orderRequest.getPrice())
            .build();
    }

    default OrderResponse toOrderResponse(OrderEntity orderEntity) {
        OrderResponse or = OrderResponse.builder()
            .id(orderEntity.getId())
            .name(orderEntity.getName())
            .price(orderEntity.getPrice())
            .build();
        return or;
    }
}
