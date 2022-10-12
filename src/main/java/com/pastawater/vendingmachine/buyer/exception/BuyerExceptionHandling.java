package com.pastawater.vendingmachine.buyer.exception;

import com.pastawater.vendingmachine.common.exception.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BuyerExceptionHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BuyerException.class})
    public ResponseEntity<ApiError> handleExceptions(BuyerException e) {
        ApiError error = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
