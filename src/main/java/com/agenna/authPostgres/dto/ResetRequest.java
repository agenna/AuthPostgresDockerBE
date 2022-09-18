package com.agenna.authPostgres.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetRequest {
    private String token;
    private String password;
    @JsonProperty("password_confirm")
    private String passwordConfirm;
}
