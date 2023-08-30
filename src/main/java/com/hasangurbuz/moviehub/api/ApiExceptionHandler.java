package com.hasangurbuz.moviehub.api;

import org.openapitools.model.ErrorDTO;
import org.openapitools.model.ErrorTagDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDTO> handle(ApiException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setTag(ErrorTagDTO.fromValue(ex.getTag().toString()));
        error.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleDtoException(BindException ex) {
        String field = ex.getBindingResult().getFieldError().getField();
        ErrorDTO error = new ErrorDTO();
        error.setTag(ErrorTagDTO.INVALID_INPUT);
        error.setMessage(field + " required");
        return ResponseEntity.badRequest().body(error);
    }


}
