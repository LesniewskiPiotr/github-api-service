package com.example.client;

import com.example.exception.UserException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.NOT_FOUND.value()) {
            return new UserException(HttpStatus.NOT_FOUND, "Resource not found");
        }
        if (response.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return new UserException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}