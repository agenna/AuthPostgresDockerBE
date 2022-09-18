package com.agenna.authPostgres.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwoFactorRequest {
    private Long id;
    private String secret;
    private String code;

    public TwoFactorRequest(Long id,String secret,String code){
        this.id=id;
        this.secret=secret;
        this.code=code;
    }       
}
