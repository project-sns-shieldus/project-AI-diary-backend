package com.restapi.project_AI_diary_backend.domain.diary.repository;


import com.restapi.project_AI_diary_backend.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Optional<Diary> findById(long diaryId);

    List<Diary> findByDiaryDate(LocalDate diaryDate);

    List<Diary> findAll();

    List<Diary> findByUserId(long userId);
}
