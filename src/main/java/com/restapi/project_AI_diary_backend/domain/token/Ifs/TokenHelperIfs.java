package com.restapi.project_AI_diary_backend.domain.token.Ifs;


import com.restapi.project_AI_diary_backend.domain.token.dto.TokenDto;

import java.util.Map;

public interface TokenHelperIfs {

    TokenDto issueAccessToken(Map<String, Object> data);

    TokenDto issueRefreshToken(Map<String, Object> data);

    Map<String, Object> validationTokenWithThrow(String token);
}
