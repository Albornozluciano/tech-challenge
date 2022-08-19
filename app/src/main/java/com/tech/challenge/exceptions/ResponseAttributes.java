package com.tech.challenge.exceptions;

import org.springframework.http.HttpStatus;

public enum ResponseAttributes {
    RESOURCE_NOT_FOUND(
            "/errors/resource_not_found",
            "Resource not found.",
            HttpStatus.NOT_FOUND
    ),
    UNKNOWN_SERVER_ERROR(
            "/errors/external_error",
            "There was an unknown server error.",
            HttpStatus.INTERNAL_SERVER_ERROR
    );

    private final String type;
    private final String title;
    private final HttpStatus status;

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public HttpStatus getStatus() {
        return status;
    }

    ResponseAttributes(String type, String title, HttpStatus status) {
        this.type = type;
        this.title = title;
        this.status = status;
    }
}