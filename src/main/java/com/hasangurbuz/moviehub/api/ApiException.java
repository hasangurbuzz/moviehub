package com.hasangurbuz.moviehub.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException extends RuntimeException {
    private ApiExceptionTag tag;
    private String message;

    public static ApiException invalidInput(String message) {
        ApiException ex = new ApiException();
        ex.setTag(ApiExceptionTag.INVALID_INPUT);
        ex.setMessage(message);
        return ex;
    }

    public static ApiException notFound(String message) {
        ApiException ex = new ApiException();
        ex.setTag(ApiExceptionTag.NOT_FOUND);
        ex.setMessage(message);
        return ex;
    }
}
