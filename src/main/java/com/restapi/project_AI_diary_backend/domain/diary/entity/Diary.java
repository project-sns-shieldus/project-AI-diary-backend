package com.restapi.project_AI_diary_backend.domain.diary.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "diary")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private long diaryId;

    @Column(name = "user_id")
    private long userId;

    @NotNull
    @Column(name = "diary_date")
    private LocalDateTime diaryDate;  // timestamp in the database

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_ai")
    private boolean isAi;

    @Column(name = "title")
    private String title;

    @Column(name = "weather")
    private String weather;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "emotion_id")
    private long emotionId;
}
