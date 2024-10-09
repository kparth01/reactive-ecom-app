package com.byondxtremes.ecom.order.repository;

import com.byondxtremes.ecom.order.domain.entity.OrderEntity;
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
public class OrderRepository {

    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
    private final DynamoDbAsyncTable<OrderEntity> orderEntityDynamoDbAsyncTable;

    public OrderRepository(
        @Qualifier("dynamoDbEnhancedAsyncClient") DynamoDbEnhancedAsyncClient enhancedAsyncClient,
        @Value("${amazon.dynamodb.tableName}") String orderTable) {
        this.enhancedAsyncClient = enhancedAsyncClient;
        this.orderEntityDynamoDbAsyncTable =
            enhancedAsyncClient.table(orderTable, TableSchema.fromBean(OrderEntity.class));

    }

    private Key getKeyBuild(String id) {
        return Key.builder().partitionValue(id).build();
    }

    public CompletableFuture<Void> save(OrderEntity productEntity) {
        return orderEntityDynamoDbAsyncTable.putItem(productEntity);
    }

    public SdkPublisher<Page<OrderEntity>> getAllOrders() {
        return orderEntityDynamoDbAsyncTable.scan();
    }

    public PagePublisher<OrderEntity> getOrderById(String id) {
        QueryConditional conditional = QueryConditional
            .keyEqualTo(Key.builder().partitionValue(id).build());

        QueryEnhancedRequest qer = QueryEnhancedRequest.builder()
            .queryConditional(conditional)
            .limit(1)
            .build();

        return orderEntityDynamoDbAsyncTable.query(qer);
    }

}
