package com.restapi.project_AI_diary_backend.common.exceptionhandler;


import com.restapi.project_AI_diary_backend.common.api.Api;
import com.restapi.project_AI_diary_backend.common.error.ErrorCode;
import com.restapi.project_AI_diary_backend.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)   // 가장 마지막에 실행 적용
public class GlobalExceptionHandler {

    // ApiException을 처리하는 예외 처리기 추가
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Api<Object>> handleApiException(ApiException ex) {
        log.error("API Exception: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(ex.getErrorCodeIfs().getHttpStatusCode())
                .body(Api.ERROR(ex.getErrorCodeIfs(), ex.getErrorDescription()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Api<Object>> exception(Exception exception) {
        log.error("Unhandled exception: ", exception);

        return ResponseEntity
                .status(500)
                .body(
                        Api.ERROR(ErrorCode.SERVER_ERROR, exception.getMessage())
                );
    }
}
