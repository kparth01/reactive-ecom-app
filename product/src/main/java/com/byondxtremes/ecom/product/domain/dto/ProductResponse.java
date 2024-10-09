package com.byondxtremes.ecom.product.domain.dto;

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
public class ProductResponse implements Serializable {

    private String id;
    private String name;
    private String price;
    private String stock;


}
