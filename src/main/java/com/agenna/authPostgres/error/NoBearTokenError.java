package com.agenna.authPostgres.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoBearTokenError extends ResponseStatusException{

    public NoBearTokenError() {
        super(HttpStatus.BAD_REQUEST, "Invalid credential error");
    }
}
