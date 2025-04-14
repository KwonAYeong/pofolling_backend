package com.kkks.pofolling.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn("[BusinessException] code={}, message={}", ex.getExceptionCode().getCode(), ex.getMessage());

        return ResponseEntity
                .status(ex.getExceptionCode().getStatus())
                .body(ErrorResponse.of(ex.getExceptionCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("[UnhandledException]", ex);

        return ResponseEntity
                .status(ExceptionCode.UNKNOWN_ERROR.getStatus())
                .body(ErrorResponse.of(ExceptionCode.UNKNOWN_ERROR));
    }
}
