package com.murilofarias.assembleiaapi.domain.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;


@Getter
public class DomainException extends ResponseStatusException {

    public DomainException(String errorMessage,  List<String> errors) {
        super(UNPROCESSABLE_ENTITY, errorMessage);
        this.errors = errors;
    }

    public DomainException(String errorMessage,  String error) {
        super(UNPROCESSABLE_ENTITY, errorMessage);
        errors = Arrays.asList(error);
    }

    private List<String> errors;
}
