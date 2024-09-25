package com.restapi.project_AI_diary_backend.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode implements ErrorCodeIfs {

    OK(200, 200, "성공"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 400, "잘못된 요청"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "서버에러"),
    NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), 512, "Null point"),

    // NOT_FOUND 오류 코드 추가
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), 404, "존재하지 않는 사용자입니다.");

    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
