package com.byondxtremes.ecom.product.repository;

import com.byondxtremes.ecom.product.domain.entity.ProductEntity;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

@Repository
public class ProductRepository {

    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
    private final DynamoDbAsyncTable<ProductEntity> productEntityDynamoDbAsyncTable;

    public ProductRepository(
        @Qualifier("dynamoDbEnhancedAsyncClient") DynamoDbEnhancedAsyncClient enhancedAsyncClient,
        @Value("${amazon.dynamodb.tableName}") String productTable) {
        this.enhancedAsyncClient = enhancedAsyncClient;
        this.productEntityDynamoDbAsyncTable =
            enhancedAsyncClient.table(productTable, TableSchema.fromBean(ProductEntity.class));

    }

    private Key getKeyBuild(String id) {
        return Key.builder().partitionValue(id).build();
    }

    public CompletableFuture<Void> save(ProductEntity productEntity) {
        return productEntityDynamoDbAsyncTable.putItem(productEntity);
    }

    public SdkPublisher<Page<ProductEntity>> getAllProducts() {
        return productEntityDynamoDbAsyncTable.scan();
    }

    public PagePublisher<ProductEntity> getProductById(String id) {
        QueryConditional conditional = QueryConditional
            .keyEqualTo(Key.builder().partitionValue(id).build());

        QueryEnhancedRequest qer = QueryEnhancedRequest.builder()
            .queryConditional(conditional)
            .limit(1)
            .build();

        return productEntityDynamoDbAsyncTable.query(qer);
    }

}
