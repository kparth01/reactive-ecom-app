package com.byondxtremes.ecom.order.domain;

import java.io.Serializable;
import lombok.Data;


@Data
public class BaseResponse implements Serializable {

    private String status;
    private Object data;
    private Object errors;


}
