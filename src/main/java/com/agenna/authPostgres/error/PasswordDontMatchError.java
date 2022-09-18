package com.agenna.authPostgres.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PasswordDontMatchError extends ResponseStatusException {

    public PasswordDontMatchError() {
        super(HttpStatus.BAD_REQUEST, "Passwords do not match");
    }
    
}
