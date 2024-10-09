package com.byondxtremes.ecom.auth.domain.entity;

import com.byondxtremes.ecom.auth.enums.ERole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean()
public class Role {
    private String id;
    private ERole name;
}

