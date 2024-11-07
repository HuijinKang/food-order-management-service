package org.sparta.foodordermanagementservice.common.exeption;


import lombok.extern.slf4j.Slf4j;
import org.sparta.foodordermanagementservice.common.ApiResponse;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /* Custom Exception */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ApiResponse<Void>> handleCustomException(final CustomException e) {
        log.error("handleCustomException: {}", e.getErrorCode());
        return new ResponseEntity<>(ApiResponse.ofError(e.getErrorCode()), e.getErrorCode().getStatus());
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException() {
        return new ResponseEntity<>(ApiResponse.ofError(ErrorCode.METHOD_NOT_ALLOWED), ErrorCode.METHOD_NOT_ALLOWED.getStatus());
    }


    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<ApiResponse<Void>> handleBadCredentialsException() {
        return new ResponseEntity<>(ApiResponse.ofError(ErrorCode.FAIL_LOGIN), ErrorCode.FAIL_LOGIN.getStatus());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> methodArgumentValidException() {
        return new ResponseEntity<>(ApiResponse.ofError(ErrorCode.BAD_REQUEST), ErrorCode.BAD_REQUEST.getStatus());
    }


    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<ApiResponse<Void>> handleQueryTimeoutException(QueryTimeoutException e) {
        log.error("fail to execute query, {}", e.getMessage());
        return new ResponseEntity<>(ApiResponse.ofError(ErrorCode.INTERNAL_SERVER_ERROR), ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }


    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ApiResponse<Void>> handleRuntimeException(final RuntimeException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(ApiResponse.ofError(ErrorCode.INTERNAL_SERVER_ERROR), ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }

}