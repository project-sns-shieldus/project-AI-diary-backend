package com.restapi.project_AI_diary_backend.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiaryResponse {
    private int diaryId;
    private String title;
    private String content;
    private LocalDate createdDate;
    private String contentCheck;
    private LocalDateTime updatedDateTime;
}
