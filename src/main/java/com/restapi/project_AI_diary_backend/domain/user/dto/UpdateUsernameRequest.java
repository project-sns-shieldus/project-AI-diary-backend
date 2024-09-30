package com.restapi.project_AI_diary_backend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class UpdateUsernameRequest {
    private String email;
    private String newUsername;
}
