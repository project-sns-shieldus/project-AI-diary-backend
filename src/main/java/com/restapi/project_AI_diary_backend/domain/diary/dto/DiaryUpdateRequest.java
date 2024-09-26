package com.restapi.project_AI_diary_backend.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiaryUpdateRequest {
    private LocalDate diaryDate;
    private String title;
    private String weather;
    private String notes;
    private LocalDateTime updatedAt = LocalDateTime.now();
}
