package com.example.spring3.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001,"invalid message key",HttpStatus.BAD_REQUEST),
    USER_EXIST(1002,"user existed",HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST(1003,"user not existed",HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1004,"user must be at least {min} characters",HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1005,"password must be at least {min} characters",HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006,"unauthenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007,"don't have permission",HttpStatus.FORBIDDEN),
    INVALID_DOB(1008,"your age must be at least {min}",HttpStatus.BAD_REQUEST),
    MOVIE_NOT_EXIST(2001,"movie not existed",HttpStatus.BAD_REQUEST)

    ;
    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
