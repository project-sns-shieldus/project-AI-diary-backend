package com.restapi.project_AI_diary_backend.domain.user.business;


import com.restapi.project_AI_diary_backend.common.annotation.Business;
import com.restapi.project_AI_diary_backend.common.error.ErrorCode;
import com.restapi.project_AI_diary_backend.common.exception.ApiException;
import com.restapi.project_AI_diary_backend.domain.user.dto.*;
import com.restapi.project_AI_diary_backend.domain.user.entity.User;
import com.restapi.project_AI_diary_backend.domain.token.business.TokenBusiness;
import com.restapi.project_AI_diary_backend.domain.token.controller.model.TokenResponse;
import com.restapi.project_AI_diary_backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Business
public class UserBusiness {
    // UserService로 이동 전 로직 처리

    private final UserService userService;
    private final UserMapper userMapper;
    private final TokenBusiness tokenBusiness;
    private final BCryptPasswordEncoder passwordEncoder;  // BCryptPasswordEncoder 주입

    // 로그인 ID를 사용하여 사용자 정보를 조회하고, DTO로 변환합니다.
    public UserResponse info(String loginId) {
        var userEntity = userService.getUserWithThrow(loginId);
        return userMapper.toResponse(userEntity);
    }

    // 로그인 ID를 사용하여 username을 반환합니다.
    public String getUsernameByLoginId(String loginId) {
        return userService.findUsernameByLoginId(loginId);
    }

    /**
     * 1. 회원가입(request) -> entity
     * 2. entity -> 데이터베이스에 저장
     * 3. 저장된 엔티티 -> response
     * 4. response 반환
     */
    public UserResponse register(UserRegisterRequest request) {
        var entity = userMapper.toEntity(request);
        var newEntity = userService.register(entity);
        var response = userMapper.toResponse(newEntity);
        return response;
    }

    public void changePassword(ChangePasswordRequest request, String token) {
        String email = tokenBusiness.getEmailFromToken(token); // 토큰에서 이메일 추출
        User user = userService.getUserWithThrow(email);

        // 현재 비밀번호가 맞는지 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호 설정
        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedNewPassword);
        user.setUpdatedAt(LocalDateTime.now());
        userService.save(user);
    }
    public void updateUsernameAndEmail(String currentEmail, UpdateUserRequest request) {
        // 현재 사용자 조회
        User user = userService.getUserWithThrow(currentEmail);

        // 새로 입력된 이메일이 이미 존재하는지 확인
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            boolean emailExists = userService.checkEmailExists(request.getEmail());
            if (emailExists) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "이미 존재하는 이메일입니다.");
            }
            user.setEmail(request.getEmail());
        }

        // 닉네임 업데이트
        if (request.getNewUsername() != null) {
            user.setUsername(request.getNewUsername());
        }

        user.setUpdatedAt(LocalDateTime.now());
        userService.save(user);
    }


    /**
     * 1. userId, password 를 가지고 사용자 체크
     * 2. user entity 로그인 확인
     * 3. token 생성(토큰 비즈니스의 이슈토큰)
     * 4. token response
     */
    public TokenResponse login(UserLoginRequest request) {
        var userEntity = userService.login(request);
        var tokenResponse = tokenBusiness.issueToken(userEntity);
        return tokenResponse;
    }

    // 로그인 후 세션에 저장된 회원의 userId를 이용하여 response(닉네임, 가입날짜 등) 정보를 받고자 하는 메소드
    public UserResponse info(
            User user
    ) {
        var userEntity = userService.getUserWithThrow(user.getEmail());
        var response = userMapper.toResponse(userEntity);
        return response;
    }
}
