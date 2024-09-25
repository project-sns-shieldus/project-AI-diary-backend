package com.restapi.project_AI_diary_backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class UserDto {

    private Long userId;

    private String username;

    private String email;

    private String password;

    private LocalDateTime lastDiaryAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
