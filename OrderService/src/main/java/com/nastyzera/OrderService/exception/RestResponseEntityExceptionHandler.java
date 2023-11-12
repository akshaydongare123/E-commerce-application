package com.nastyzera.OrderService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nastyzera.OrderService.external.response.ErrorResponse;


@ControllerAdvice
public class RestResponseEntityExceptionHandler  extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomServiceException(CustomException exception) {
            return new ResponseEntity<>(new ErrorResponse().builder()
                    .errorMessage(exception.getMessage())
                    .errorCode(exception.getErrorCode())
                    .build(), HttpStatus.valueOf(exception.getStatus()));
    }
}
