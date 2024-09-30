package com.restapi.project_AI_diary_backend.domain.user.controller;


import com.restapi.project_AI_diary_backend.common.api.Api;
import com.restapi.project_AI_diary_backend.common.error.ErrorCode;
import com.restapi.project_AI_diary_backend.common.exception.ApiException;
import com.restapi.project_AI_diary_backend.domain.token.business.TokenBusiness;
import com.restapi.project_AI_diary_backend.domain.user.business.UserBusiness;
import com.restapi.project_AI_diary_backend.domain.user.dto.UpdateUsernameRequest;
import com.restapi.project_AI_diary_backend.domain.user.dto.UserDto;
import com.restapi.project_AI_diary_backend.domain.user.dto.UserResponse;
import com.restapi.project_AI_diary_backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins="http://localhost:3000", allowedHeaders="POST")
public class UserApiController {

    private final UserBusiness userBusiness;
    private final TokenBusiness tokenBusiness;
    private final UserService userService;


    @GetMapping("/users")
    public Api<String> getUsername(@RequestHeader("Authorization") String accessToken) {
        // 토큰을 검증하여 로그인 ID를 가져옵니다.
        String loginId = tokenBusiness.validationAccessToken(accessToken);
        if (loginId == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "인증 실패");
        }

        // 로그인 ID를 사용하여 사용자 이름을 조회
        String username = userBusiness.getUsernameByLoginId(loginId);
        return Api.OK(username);
    }

    // 이메일을 통한 사용자 정보 업데이트
    @PutMapping("/update-username")
    public ResponseEntity<Api<UserDto>> updateUsername(
            @RequestBody UpdateUsernameRequest request, // @RequestBody로 받음
            @RequestHeader("Authorization") String accessToken // 인증 토큰
    ) {
        // 토큰 검증
        String loginId = tokenBusiness.validationAccessToken(accessToken);
        if (loginId == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "인증 실패");
        }

        // 이메일로 사용자 정보 업데이트
        UserDto updatedUser = userService.updateUser(request.getEmail(), request.getNewUsername());

        return ResponseEntity.ok(Api.OK(updatedUser));
    }

}
