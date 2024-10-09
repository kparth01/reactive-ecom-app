package com.byondxtremes.ecom.order.domain.dto;

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
public class OrderResponse implements Serializable {

    private String id;
    private String name;
    private String price;
    
}
