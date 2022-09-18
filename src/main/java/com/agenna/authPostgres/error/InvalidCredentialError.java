package com.agenna.authPostgres.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidCredentialError extends ResponseStatusException{

    public InvalidCredentialError() {
        super(HttpStatus.BAD_REQUEST, "Invalid credential error");
    }

    
}
