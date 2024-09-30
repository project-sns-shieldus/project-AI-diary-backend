package com.restapi.project_AI_diary_backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long userId;

    private String username;

    private String email;

    private String password;

    private LocalDateTime lastDiaryAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 추가 생성자: userId, username, email만 초기화하는 생성자
    public UserResponse(Long userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}
