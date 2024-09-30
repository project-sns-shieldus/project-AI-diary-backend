package com.restapi.project_AI_diary_backend.domain.token.business;

import com.restapi.project_AI_diary_backend.common.annotation.Business;
import com.restapi.project_AI_diary_backend.common.error.ErrorCode;
import com.restapi.project_AI_diary_backend.common.exception.ApiException;
import com.restapi.project_AI_diary_backend.domain.user.entity.User;
import com.restapi.project_AI_diary_backend.domain.token.controller.model.TokenResponse;
import com.restapi.project_AI_diary_backend.domain.token.converter.TokenConverter;
import com.restapi.project_AI_diary_backend.domain.token.service.TokenService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@Business
public class TokenBusiness {

    private final TokenService tokenService;

    private final TokenConverter tokenConverter;


    /**
     * 1. user entity user Id 추출
     * 2. access, refresh token 발행
     * 3. converter -> token response로 변경
     */

    public TokenResponse issueToken(User userEntity) {
        // userEntity가 null인 경우 예외를 발생시킵니다.
        if (userEntity == null) {
            throw new ApiException(ErrorCode.NULL_POINT);
        }

        // userEntity에서 이메일과 사용자 이름을 추출
        String email = userEntity.getEmail();
        if (email == null || email.isEmpty()) {
            throw new ApiException(ErrorCode.NULL_POINT); // 적절한 예외 처리
        }

        // accessToken과 refreshToken 발행
        var accessToken = tokenService.issueAccessToken(email);
        var refreshToken = tokenService.issueRefreshToken(email);

        // TokenResponse로 변환하여 반환
        return tokenConverter.toResponse(accessToken, refreshToken);
    }


    public String validationAccessToken(String accessToken){
        var email = tokenService.validationToken(accessToken);
        return email;
    }

    public String getEmailFromToken(String token) {
        // TokenService에서 이메일을 추출하는 메서드를 호출
        return tokenService.validationToken(token);
    }


}
