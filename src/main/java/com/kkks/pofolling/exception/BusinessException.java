package com.kkks.pofolling.exception;

import ch.qos.logback.core.sift.AppenderFactoryUsingSiftModel;

public class BusinessException extends RuntimeException{

    private final ExceptionCode exceptionCode;

    public BusinessException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
