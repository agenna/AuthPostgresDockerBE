package com.agenna.authPostgres.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwoFactorResponse {
    private String token;

    public TwoFactorResponse(String token){
        this.token=token;
    }        
}
