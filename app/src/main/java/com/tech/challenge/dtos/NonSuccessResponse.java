package com.tech.challenge.dtos;

import com.tech.challenge.exceptions.ResponseAttributes;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NonSuccessResponse {
    private String type;
    private String title;
    private int statusCode;
    private String details;

    public NonSuccessResponse(ResponseAttributes responseAttributes, String message) {
        this.type = responseAttributes.getType();
        this.title = responseAttributes.getTitle();
        this.statusCode = responseAttributes.getStatus().value();
        this.details = message;
    }
}