package com.restapi.project_AI_diary_backend.domain.token.business;

import com.restapi.project_AI_diary_backend.common.annotation.Business;
import com.restapi.project_AI_diary_backend.common.error.ErrorCode;
import com.restapi.project_AI_diary_backend.common.exception.ApiException;
import com.restapi.project_AI_diary_backend.db.user.User;
import com.restapi.project_AI_diary_backend.domain.token.controller.model.TokenResponse;
import com.restapi.project_AI_diary_backend.domain.token.converter.TokenConverter;
import com.restapi.project_AI_diary_backend.domain.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    public TokenResponse issueToken(User userEntity){

        return Optional.ofNullable(userEntity)
                .map(user -> {
                    return user.getUsername();
                })
                .map(user -> {
                    String email = userEntity.getEmail();
                    var accessToken = tokenService.issueAccessToken(email);
                    var refreshToken = tokenService.issueRefreshToken(email);
                    return tokenConverter.toResponse(accessToken, refreshToken);
                })
                .orElseThrow(
                        ()-> new ApiException(ErrorCode.NULL_POINT)
                );


    }

    public String validationAccessToken(String accessToken){
        var email = tokenService.validationToken(accessToken);
        return email;
    }

}
