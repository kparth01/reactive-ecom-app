package com.byondxtremes.ecom.shipping.domain.dto;

import java.io.Serializable;
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
public class ShippingResponse implements Serializable {

    private String id;
    private String orderId;
    private String shippingDate;
    private String status;


}
