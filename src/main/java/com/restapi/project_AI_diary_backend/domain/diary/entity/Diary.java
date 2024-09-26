package com.restapi.project_AI_diary_backend.domain.diary.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
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
    private LocalDate diaryDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_ai")
    private boolean isAi;

    @Column(name = "title")
    private String title;

    @Column(name = "weather")
    private String weather;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
