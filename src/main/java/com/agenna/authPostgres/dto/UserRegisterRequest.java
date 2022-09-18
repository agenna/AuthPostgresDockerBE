package com.agenna.authPostgres.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String email;
    private String password;
    @JsonProperty("password_confirm")
    private String passwordConfirm;
}
