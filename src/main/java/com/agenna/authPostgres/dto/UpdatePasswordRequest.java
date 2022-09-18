package com.agenna.authPostgres.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {
    private String email;
    private String password;
    @JsonProperty("confirm_password")
    private String passwordConfirm;    
}
