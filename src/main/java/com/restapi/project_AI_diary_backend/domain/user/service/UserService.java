package com.restapi.project_AI_diary_backend.domain.user.service;

import com.restapi.project_AI_diary_backend.common.error.ErrorCode;
import com.restapi.project_AI_diary_backend.common.exception.ApiException;
import com.restapi.project_AI_diary_backend.domain.user.entity.User;
import com.restapi.project_AI_diary_backend.domain.user.repository.UserRepository;
import com.restapi.project_AI_diary_backend.domain.user.dto.UserDto;
import com.restapi.project_AI_diary_backend.domain.user.dto.UserLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원가입 로직
    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // 로그인 로직
    public User login(UserLoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        user.setLastDiaryAt(LocalDateTime.now());
        userRepository.save(user);
        return user;
    }

    // 이메일로 사용자 정보 조회
    public User getUserWithThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));
    }

    // 로그인 ID로 닉네임 반환
    public String findUsernameByLoginId(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getUsername();
    }

    // 닉네임으로 사용자 검색
    public UserDto searchByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다."));
        return UserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .createdAt(user.getCreatedAt())
                .lastDiaryAt(user.getLastDiaryAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // 사용자 정보 저장
    public void save(User user) {
        userRepository.save(user);
    }

    // 이메일 중복 여부 확인
    public boolean checkEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
