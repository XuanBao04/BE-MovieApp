package com.example.spring3.exception;

public class AppException extends RuntimeException {
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErollCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
