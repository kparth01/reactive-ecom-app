package com.byondxtremes.ecom.common.kafka;

public class EventsUtil {

    private EventsUtil() {
    }

    public enum OrderEventsEnum {

        INIT,
        REVERT_ORDER,
        UPDATE_PRODUCT_STOCK,
        PREPARE_SHIPPING,

        ;
    }

    public enum ProductEventsEnum {

        PRODUCT_STOCK_POSTIIVE,
        PRODUCT_STOCK_NEAGATIVE,
        PRODUCT_STOCK_CHECK_FAILURE,
    }

    public enum ShippingEnum {
        SHIPPING_SUCCESS,
        SHIPPING_FAILURE,
        ;

    }
}
