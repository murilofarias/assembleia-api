package com.murilofarias.assembleiaapi.domain.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceNotFoundException extends ResponseStatusException
{
    public ResourceNotFoundException(String message)
    {
        super(HttpStatus.NOT_FOUND, message);
    }
}
