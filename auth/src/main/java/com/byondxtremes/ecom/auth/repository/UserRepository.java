package com.byondxtremes.ecom.auth.repository;

import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Repository;
import com.byondxtremes.ecom.auth.domain.entity.UserEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;


@Repository
public class UserRepository {

    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
    private final DynamoDbAsyncTable<UserEntity> userEntityDynamoDbAsyncTable;

    public UserRepository(
        @Qualifier("dynamoDbEnhancedAsyncClient") DynamoDbEnhancedAsyncClient enhancedAsyncClient,
        @Value("${amazon.dynamodb.tableName}") String authTable) {
        this.enhancedAsyncClient = enhancedAsyncClient;
        this.userEntityDynamoDbAsyncTable =
            enhancedAsyncClient.table(authTable, TableSchema.fromBean(UserEntity.class));

    }

    private Key getKeyBuild(String id) {
        return Key.builder().partitionValue(id).build();
    }

    public CompletableFuture<Void> save(UserEntity userEntitity) {
        return userEntityDynamoDbAsyncTable.putItem(userEntitity);
    }

    public PagePublisher<UserEntity> getUserById(String id) {
        QueryConditional conditional = QueryConditional
            .keyEqualTo(Key.builder().partitionValue(id).build());

        QueryEnhancedRequest qer = QueryEnhancedRequest.builder()
            .queryConditional(conditional)
            .limit(1)
            .build();

        return userEntityDynamoDbAsyncTable.query(qer);
    }
}
