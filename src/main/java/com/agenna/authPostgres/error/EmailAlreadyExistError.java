package com.agenna.authPostgres.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyExistError extends ResponseStatusException{

    public EmailAlreadyExistError() {
        super(HttpStatus.BAD_REQUEST, "Email already exists");
    }
    
}
