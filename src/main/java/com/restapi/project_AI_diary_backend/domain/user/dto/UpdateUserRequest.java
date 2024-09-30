package com.restapi.project_AI_diary_backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String email;  // 새로 업데이트할 이메일
    private String newUsername;  // 새로 업데이트할 닉네임
}
