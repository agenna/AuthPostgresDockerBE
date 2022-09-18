package com.agenna.authPostgres.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserRegisterResponse {
    private Long id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String email;

    public UserRegisterResponse(
        Long id,
        String firstName, 
        String lastName, 
        String email) {
            this.id=id;
            this.firstName= firstName;
            this.lastName=lastName;
            this.email=email;
    }

}
