package com.agenna.authPostgres.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetResponse {
    private String message;

    public ResetResponse(String message){
        this.message=message;
    }
}
