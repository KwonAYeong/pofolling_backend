package com.kkks.pofolling.edit.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    /*
        성공시 보내는 응답 객체
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), "요청 성공", data);
    }

    /*
        비즈니스 로직은 성공했으나 리소스가 없는 경우 사용하는 객체
     */
    public static <T> ApiResponse<T> successWithMessage(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    /*
        예외 발생 시 응답 객체
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}
