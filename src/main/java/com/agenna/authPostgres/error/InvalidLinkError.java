package com.agenna.authPostgres.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidLinkError extends ResponseStatusException{
    
    public InvalidLinkError() {
        super(HttpStatus.BAD_REQUEST, "Invalid link error");
    }
}