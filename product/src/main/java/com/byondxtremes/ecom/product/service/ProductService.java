package com.byondxtremes.ecom.product.service;


import com.byondxtremes.ecom.common.logging.LogUtility;
import com.byondxtremes.ecom.product.domain.dto.ProductRequest;
import com.byondxtremes.ecom.product.domain.dto.ProductResponse;
import com.byondxtremes.ecom.product.domain.entity.ProductEntity;
import com.byondxtremes.ecom.product.mapper.ProductMapper;
import com.byondxtremes.ecom.product.repository.ProductRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

@Service
public class ProductService {

    LogUtility logger = new LogUtility(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public Mono<String> createProduct(ProductRequest productRequest) {
        UUID id = UUID.randomUUID();
        ProductEntity entity = productMapper.toProductEntity(productRequest, id);
        productRepository.save(entity);
        logger.info("Product saved successfully!!");
        return Mono.just(String.valueOf(id));
    }

    public Flux<ProductResponse> getProducts() {
        SdkPublisher<Page<ProductEntity>> response = productRepository.getAllProducts();
        return Flux.from(response)
            .map(products -> products.items().stream())
            .flatMap(productEntityStream -> Flux.fromStream(productEntityStream))
            .map(productEntity -> productMapper.toProductResponse(productEntity));
    }

    public Mono<ProductResponse> getProductById(String id) {
        //publisher
        PagePublisher<ProductEntity> response = productRepository.getProductById(id);
        return Mono.from(response)
            .map(productEntityPage -> productEntityPage.items().get(0))
            .map(productEntity -> productMapper.toProductResponse(productEntity));
    }

    public Mono<ProductEntity> getProductEntityById(String id) {
        PagePublisher<ProductEntity> response = productRepository.getProductById(id);
        return Mono.from(response).map(product -> product.items().get(0));
    }
}

