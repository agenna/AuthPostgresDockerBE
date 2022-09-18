package com.agenna.authPostgres.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private Long id;
    private String secret;
    @JsonProperty("otp_auth_url")
    private String otpAuthUrl;

    public LoginResponse(Long id,String secret,String otpAuthUrl){
        this.id=id;
        this.secret=secret;
        this.otpAuthUrl=otpAuthUrl;
    }
}
