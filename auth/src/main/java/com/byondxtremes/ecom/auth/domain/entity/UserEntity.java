package com.byondxtremes.ecom.auth.domain.entity;

import java.util.ArrayList;
import java.util.List;
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
public class UserEntity {
    private String username;
    private String email;
    private String password;
    private List<Role> roles = new ArrayList<>();

    @DynamoDbAttribute("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DynamoDbAttribute("roles")
    @DynamoDBProperty(Converter = typeof(ListToSetPropertyConverter<byte>)
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
