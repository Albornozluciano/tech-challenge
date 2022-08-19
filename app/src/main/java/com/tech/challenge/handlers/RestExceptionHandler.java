package com.tech.challenge.handlers;

import com.tech.challenge.dtos.NonSuccessResponse;
import com.tech.challenge.exceptions.NonSuccessException;
import com.tech.challenge.exceptions.ResponseAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(NonSuccessException.class)
    ResponseEntity<NonSuccessResponse> nonSuccessException(NonSuccessException ex) {
        if (ex.getNonSuccessResponse().getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            log.error("Unknown non success error.", ex);
        }
        return new ResponseEntity<>(
            ex.getNonSuccessResponse(),
            HttpStatus.valueOf(ex.getNonSuccessResponse().getStatusCode())
        );
    }

    @ExceptionHandler(Throwable.class)
    ResponseEntity<NonSuccessResponse> genericException(Exception ex) {
        log.error("Unknown server error.", ex);
        return new ResponseEntity<>(
            new NonSuccessResponse(
                ResponseAttributes.UNKNOWN_SERVER_ERROR,
                "Unknown server error. See logs."),
                ResponseAttributes.UNKNOWN_SERVER_ERROR.getStatus()
        );
    }
}