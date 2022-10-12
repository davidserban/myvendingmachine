package com.pastawater.vendingmachine.common.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiError> handleConstraintViolationException() {
        ApiError error = new ApiError("Invalid request data", HttpStatus.PRECONDITION_FAILED);
        return new ResponseEntity<>(error, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException() {
        ApiError error = new ApiError("Invalid request data", HttpStatus.PRECONDITION_FAILED);
        return new ResponseEntity<>(error, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException e) {
        ApiError error = new ApiError("Not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({OperationNotPermittedException.class})
    public ResponseEntity<ApiError> handleNotPermittedOperation(OperationNotPermittedException e) {
        ApiError error = new ApiError(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }
}
