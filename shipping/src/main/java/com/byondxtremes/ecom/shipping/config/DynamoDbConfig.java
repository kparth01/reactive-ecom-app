package com.byondxtremes.ecom.shipping.config;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Configuration
public class DynamoDbConfig {

    @Value("${amazon.dynamodb.endpoint}")
    String dynamoDBEndpoint;

    @Value("${amazon.dynamodb.accessKey}")
    String accessKey;

    @Value("${amazon.dynamodb.secretKey}")
    String secretKey;

    @Bean("dynamoDbEnhancedAsyncClient")
    public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient() {
        return DynamoDbEnhancedAsyncClient.builder()
            .dynamoDbClient(getDynamoDbAsyncClient())
            .build();
    }

    @Bean("dynamoDbAsyncClient")
    public DynamoDbAsyncClient getDynamoDbAsyncClient() {
        return DynamoDbAsyncClient.builder()
            .endpointOverride(URI.create(dynamoDBEndpoint))
            .region(Region.AP_SOUTHEAST_1)
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)))
            .build();
    }
}
