package com.byondxtremes.ecom.order.utils;

public enum OrderStatusEnum {

    INIT("INITIATED"),
    ORDER_REVERTED("ORDER_REVERTED"),
    CREATED("CREATED"),
    SHIPPED("SHIPPED"),
    SHIPPING_FAILED("SHIPPING_FAILED");

    private String value;


    OrderStatusEnum(String value) {
        this.value = value;
    }
}
