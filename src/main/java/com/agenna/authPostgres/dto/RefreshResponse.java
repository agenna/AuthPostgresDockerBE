package com.agenna.authPostgres.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshResponse  {
    private String token;

    public RefreshResponse(String token){
        this.token=token;
    }
}
