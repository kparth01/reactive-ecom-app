package com.byondxtremes.ecom.shipping.repository;

import com.byondxtremes.ecom.shipping.domain.entity.ShippingEntity;
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
public class ShippingRepository {

    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
    private final DynamoDbAsyncTable<ShippingEntity> shippingEntityDynamoDbAsyncTable;

    public ShippingRepository(
        @Qualifier("dynamoDbEnhancedAsyncClient") DynamoDbEnhancedAsyncClient enhancedAsyncClient,
        @Value("${amazon.dynamodb.tableName}") String shippingTable) {
        this.enhancedAsyncClient = enhancedAsyncClient;
        this.shippingEntityDynamoDbAsyncTable =
            enhancedAsyncClient.table(shippingTable, TableSchema.fromBean(ShippingEntity.class));

    }

    private Key getKeyBuild(String id) {
        return Key.builder().partitionValue(id).build();
    }

    public CompletableFuture<Void> save(ShippingEntity productEntity) {
        return shippingEntityDynamoDbAsyncTable.putItem(productEntity);
    }

    public SdkPublisher<Page<ShippingEntity>> getAllShipping() {
        return shippingEntityDynamoDbAsyncTable.scan();
    }

    public PagePublisher<ShippingEntity> getShippingById(String id) {
        QueryConditional conditional = QueryConditional
            .keyEqualTo(Key.builder().partitionValue(id).build());

        QueryEnhancedRequest qer = QueryEnhancedRequest.builder()
            .queryConditional(conditional)
            .limit(1)
            .build();

        return shippingEntityDynamoDbAsyncTable.query(qer);
    }

}
