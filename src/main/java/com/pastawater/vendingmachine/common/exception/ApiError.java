package com.pastawater.vendingmachine.common.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiError {
    private final String message;
    private final HttpStatus status;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
