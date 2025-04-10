package com.kkks.pofolling.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.SplittableRandom;

@Getter @Builder
public class ErrorResponse {

    private final int status;
    private final String code;
    private final String message;

    public static ErrorResponse of(ExceptionCode code) {
        return ErrorResponse.builder()
                .status(code.getStatus())
                .code(code.getCode())
                .message(code.getMessage())
                .build();
    }
}
