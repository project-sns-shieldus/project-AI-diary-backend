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

    public User register(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.now());  // createdAt 설정
        user.setUpdatedAt(LocalDateTime.now());  // updatedAt 설정
        return userRepository.save(user);
    }

    public User login(UserLoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));

        boolean passwordMatch = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if (!passwordMatch) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        user.setLastDiaryAt(LocalDateTime.now()); // 로그인 성공 시 lastDiaryAt 업데이트
        userRepository.save(user);

        return user;
    }

    public User getUserWithThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));
    }

    public String findUsernameByLoginId(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getUsername();
    }

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

    public UserDto searchByEmail(String email) {
        // 이메일로 사용자 검색
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // User 엔티티를 UserDto로 변환하여 반환
        return new UserDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getLastDiaryAt(), user.getCreatedAt(), user.getUpdatedAt());
    }

    // 사용자 정보 업데이트 메서드 추가
    public UserDto updateUser(String email, String newUsername) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        user.setUsername(newUsername);
        userRepository.save(user);

        // 업데이트된 사용자 정보를 반환
        return new UserDto(user.getUserId(), user.getUsername(), user.getEmail());
    }
}
