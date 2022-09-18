package com.agenna.authPostgres.dto;

import com.agenna.authPostgres.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    
    private Long id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String email;

    public UserResponse(
        Long id,
        String firstName, 
        String lastName, 
        String email) {
            this.id=id;
            this.firstName= firstName;
            this.lastName=lastName;
            this.email=email;
    }   

    public UserResponse(
        User user) {
            this.id=user.getId();
            this.firstName= user.getFirstName();
            this.lastName=user.getLastName();
            this.email=user.getEmail();
    }   

}
