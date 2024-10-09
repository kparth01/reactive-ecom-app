package com.byondxtremes.ecom.product.mapper;

import com.byondxtremes.ecom.product.domain.dto.ProductRequest;
import com.byondxtremes.ecom.product.domain.dto.ProductResponse;
import com.byondxtremes.ecom.product.domain.entity.ProductEntity;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring"
)
public interface ProductMapper {

    default ProductEntity toProductEntity(ProductRequest productRequest, UUID id) {
        return ProductEntity.builder()
            .id(String.valueOf(id))
            .name(productRequest.getName())
            .price(productRequest.getPrice())
            .stock(productRequest.getStock())
            .build();
    }

    default ProductResponse toProductResponse(ProductEntity productEntity) {
        ProductResponse pe = ProductResponse.builder()
            .id(productEntity.getId())
            .name(productEntity.getName())
            .price(productEntity.getPrice())
            .stock(productEntity.getStock())
            .build();
        return pe;
    }
}
