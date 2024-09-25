package com.restapi.project_AI_diary_backend.domain.user.dto;

import com.restapi.project_AI_diary_backend.common.error.ErrorCode;
import com.restapi.project_AI_diary_backend.common.exception.ApiException;
import com.restapi.project_AI_diary_backend.db.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserMapper {

    public User toEntity(UserRegisterRequest request){

        // Request 데이터 유효성 검사 추가
        if(request == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "UserRegisterRequest Null");
        }

        // 엔티티로 변환
        return User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
    }

    public UserResponse toResponse(User user) {

        return Optional.ofNullable(user)
                .map(it -> UserResponse.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .lastDiaryAt(user.getLastDiaryAt())
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .build())
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "UserEntity Null"));
    }
}
