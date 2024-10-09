package com.byondxtremes.ecom.shipping.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean()
public class ShippingEntity {

    private String id;
    private String orderId;
    private String shippingDate;
    private String status;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String name) {
        this.orderId = orderId;
    }

    @DynamoDbAttribute("shippingDate")
    public String getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(String price) {
        this.shippingDate = shippingDate;
    }

    @DynamoDbAttribute("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String stock) {
        this.status = status;
    }
}
