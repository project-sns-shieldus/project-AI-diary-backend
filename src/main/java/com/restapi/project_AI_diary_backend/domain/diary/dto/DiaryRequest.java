package com.restapi.project_AI_diary_backend.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiaryRequest {
    private LocalDate diaryDate;
    private String title;
    private String weather;
    private String notes;
    private LocalDate createdAt = LocalDate.now();
}
