package com.example.onedaypiece.exception;


// api 관련 요청 exception 처리
// 비즈니스 예외처리
public class ApiRequestException extends IllegalArgumentException {
    public ApiRequestException(String message) {
        super(message);
    }

    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}