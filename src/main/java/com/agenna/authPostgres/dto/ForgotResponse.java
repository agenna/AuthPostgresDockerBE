package com.agenna.authPostgres.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotResponse {
    private String message;

    public ForgotResponse(String message){
        this.message=message;
    }
}
