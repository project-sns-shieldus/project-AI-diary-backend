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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Business
public class UserBusiness {

    private final UserService userService;
    private final UserMapper userMapper;
    private final TokenBusiness tokenBusiness;
    private final BCryptPasswordEncoder passwordEncoder;
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

    // 회원가입 로직
    public UserResponse register(UserRegisterRequest request) {
        var entity = userMapper.toEntity(request);
        var newEntity = userService.register(entity);
        return userMapper.toResponse(newEntity);
    }

    // 비밀번호 변경 로직
    public void changePassword(ChangePasswordRequest request, String token) {
        String email = tokenBusiness.getEmailFromToken(token);
        User user = userService.getUserWithThrow(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다.");
        }

        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedNewPassword);
        user.setUpdatedAt(LocalDateTime.now());
        userService.save(user);
    }

    // 이메일 및 닉네임 업데이트 로직
    public void updateUsernameAndEmail(String currentEmail, UpdateUserRequest request) {
        User user = userService.getUserWithThrow(currentEmail);

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            boolean emailExists = userService.checkEmailExists(request.getEmail());
            if (emailExists) {
                throw new ApiException(ErrorCode.BAD_REQUEST, "이미 존재하는 이메일입니다.");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getNewUsername() != null) {
            user.setUsername(request.getNewUsername());
        }

        user.setUpdatedAt(LocalDateTime.now());
        userService.save(user);
    }

    // 로그인 로직
    public TokenResponse login(UserLoginRequest request) {
        var userEntity = userService.login(request);
        return tokenBusiness.issueToken(userEntity);
    }

    public UserResponse updateUser(String username, UserDto userDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다."));

        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        userRepository.save(user);

        // UserResponse 생성자에 맞는 필드를 모두 전달합니다.
        return new UserResponse(user.getUserId(), user.getUsername(), user.getEmail(),
                user.getPassword(), user.getLastDiaryAt(),
                user.getCreatedAt(), user.getUpdatedAt());
    }
}
