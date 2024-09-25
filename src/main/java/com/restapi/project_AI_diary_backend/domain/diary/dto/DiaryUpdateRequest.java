package com.restapi.project_AI_diary_backend.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiaryUpdateRequest {
    private String title;
    private String content;
    private LocalDateTime updatedDateTime = LocalDateTime.now();
}
