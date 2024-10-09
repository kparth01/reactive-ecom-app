package com.byondxtremes.ecom.auth.domain.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private final String token;
    private String username;
    private Date expireAt;
    private Boolean isTokenExpired;
    private Date issuedAt;

    public JwtResponse(String token) {
        this.token = token;
    }

}