package com.reactivespring.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandling {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleRequestBodyError(WebExchangeBindException ex) {
        log.error("Exception caught in handleRequestBodyError: {}", ex.getMessage(), ex);

        var errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .sorted()
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
