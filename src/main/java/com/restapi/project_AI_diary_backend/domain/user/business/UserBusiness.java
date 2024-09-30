package com.restapi.project_AI_diary_backend.domain.user.business;


import com.restapi.project_AI_diary_backend.common.annotation.Business;
import com.restapi.project_AI_diary_backend.common.error.ErrorCode;
import com.restapi.project_AI_diary_backend.common.exception.ApiException;
import com.restapi.project_AI_diary_backend.domain.user.dto.*;
import com.restapi.project_AI_diary_backend.domain.user.entity.User;
import com.restapi.project_AI_diary_backend.domain.token.business.TokenBusiness;
import com.restapi.project_AI_diary_backend.domain.token.controller.model.TokenResponse;
import com.restapi.project_AI_diary_backend.domain.user.repository.UserRepository;
import com.restapi.project_AI_diary_backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Business
public class UserBusiness {
    // UserService로 이동 전 로직 처리

    private final UserService userService;
    private final UserMapper userMapper;
    private final TokenBusiness tokenBusiness;
    private final UserRepository userRepository;

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

    public UserResponse updateUser(String username, UserDto userDto) {
        // 사용자 검색
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다."));

        // 사용자 정보 업데이트
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());

        // 업데이트 후 저장
        userRepository.save(user);

        // 업데이트된 사용자 정보를 반환
        return new UserResponse(user.getUserId(), user.getUsername(), user.getEmail());
    }

}
