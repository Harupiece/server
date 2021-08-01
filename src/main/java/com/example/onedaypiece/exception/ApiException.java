package com.example.onedaypiece.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

@NoArgsConstructor
@Getter
public class ApiException {
    private  String message;
    private  HttpStatus httpStatus;

    public ApiException(String message, HttpStatus badRequest) {
        this.message =message;
        this.httpStatus = badRequest;
    }

}