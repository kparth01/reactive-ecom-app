package com.byondxtremes.ecom.common.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KafkaMessage {

    private String event;
    private String productId;
    private String orderId;
    private String shippingId;

}
