package com.restapi.project_AI_diary_backend.common.exception;


import com.restapi.project_AI_diary_backend.common.error.ErrorCodeIfs;

public interface ApiExceptionIfs {

    ErrorCodeIfs getErrorCodeIfs();
    String getErrorDescription();
}
