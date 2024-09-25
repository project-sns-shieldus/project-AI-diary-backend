package com.restapi.project_AI_diary_backend.domain.diary.repository;


import com.restapi.project_AI_diary_backend.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepsitory extends JpaRepository<Diary, Integer> {
    Optional<Diary> findById(int diaryId);

    List<Diary> findByCreatedDate(LocalDate createdDate);

    List<Diary> findAll();


}
