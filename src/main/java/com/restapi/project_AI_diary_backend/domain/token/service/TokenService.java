package com.restapi.project_AI_diary_backend.domain.token.service;

import com.restapi.project_AI_diary_backend.common.error.ErrorCode;
import com.restapi.project_AI_diary_backend.common.exception.ApiException;
import com.restapi.project_AI_diary_backend.domain.token.Ifs.TokenHelperIfs;
import com.restapi.project_AI_diary_backend.domain.token.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenHelperIfs tokenHelperIfs;

    // 토큰 생성
    public TokenDto issueAccessToken(String email){
        var data = new HashMap<String, Object>();
        data.put("email", email);
        return tokenHelperIfs.issueAccessToken(data);

    }

    // 토큰 재발행
    public TokenDto issueRefreshToken(String email){
        var data = new HashMap<String, Object>();
        data.put("email", email);
        return tokenHelperIfs.issueAccessToken(data);

    }

    // 토큰 검증
    public String validationToken(String token){
        var map = tokenHelperIfs.validationTokenWithThrow(token);
        var email = map.get("email");
        Objects.requireNonNull(email, () ->{throw new ApiException(ErrorCode.NULL_POINT);});
        return email.toString();

    }

}
