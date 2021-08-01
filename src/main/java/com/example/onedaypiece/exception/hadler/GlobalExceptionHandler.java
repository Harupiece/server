package com.example.onedaypiece.exception.hadler;

import com.example.onedaypiece.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     *  Validation 예외처리
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiException handleMethodArgumentNotValidException ( MethodArgumentNotValidException  ex) {

        return new ApiException(ex.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST );
    }
}
