package com.example.spring3.exception;

import com.example.spring3.dto.request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTES = "min";

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception){
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
    return ResponseEntity.status(errorCode.getStatusCode()).body(
            ApiResponse.builder()
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage())
                    .build()
    );
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
        String enumKey = exception.getFieldError().getDefaultMessage();
        Map<String,Object> attributes = null;

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);


            var contrainViolation = exception.getBindingResult()
                    .getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = contrainViolation.getConstraintDescriptor().getAttributes();

           log.info(attributes.toString());

        }catch (IllegalArgumentException e){

        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(Objects.nonNull(attributes)?mapAttribute(errorCode.getMessage(),attributes):errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String,Object> attributes){

        String minValue =String.valueOf( attributes.get(MIN_ATTRIBUTES));

        return message.replace("{"+MIN_ATTRIBUTES+"}",minValue);

    }
}
