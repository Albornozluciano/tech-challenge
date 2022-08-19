package com.tech.challenge.exceptions;

import com.tech.challenge.dtos.NonSuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NonSuccessException extends Throwable {
    NonSuccessResponse nonSuccessResponse;
}
