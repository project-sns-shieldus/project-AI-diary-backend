package com.restapi.project_AI_diary_backend.domain.diary.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "diary")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private int diaryId;

    @NotNull
    @Column(name = "contents")
    private String content;

    //@NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column
    private LocalDate createdDate; //= LocalDateTime.now();

    @NotNull
    @Column
    private LocalDateTime updatedDatetime;
}
