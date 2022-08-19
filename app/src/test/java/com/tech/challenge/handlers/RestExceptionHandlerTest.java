package com.tech.challenge.handlers;

import com.tech.challenge.TestHelper;
import com.tech.challenge.dtos.NonSuccessResponse;
import com.tech.challenge.exceptions.NonSuccessException;
import com.tech.challenge.exceptions.ResponseAttributes;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebFluxTest(RestExceptionHandler.class)
public class RestExceptionHandlerTest extends TestHelper {
    @Test
    void getNonSuccessResponseFromNonSuccessException() {
        RestExceptionHandler restExceptionHandler = new RestExceptionHandler();
        NonSuccessResponse nonSuccessResponse = new NonSuccessResponse(ResponseAttributes.UNKNOWN_SERVER_ERROR, "details");
        NonSuccessException nonSuccessException = new NonSuccessException(nonSuccessResponse);

        ResponseEntity<NonSuccessResponse> response = restExceptionHandler.nonSuccessException(nonSuccessException);

        assertEquals(nonSuccessException.getNonSuccessResponse(), response.getBody());
        assertEquals(nonSuccessException.getNonSuccessResponse().getStatusCode(), response.getStatusCode().value());
    }

    @Test
    void getNonSuccessResponseFromGenericException() {
        RestExceptionHandler restExceptionHandler = new RestExceptionHandler();

        NonSuccessResponse nonSuccessResponse = new NonSuccessResponse(ResponseAttributes.UNKNOWN_SERVER_ERROR,
            "Unknown server error. See logs.");

        ResponseEntity<NonSuccessResponse> response = restExceptionHandler.genericException(new RuntimeException());

        assertEquals(nonSuccessResponse, response.getBody());
        assertEquals(nonSuccessResponse.getStatusCode(), response.getStatusCode().value());
    }
}
