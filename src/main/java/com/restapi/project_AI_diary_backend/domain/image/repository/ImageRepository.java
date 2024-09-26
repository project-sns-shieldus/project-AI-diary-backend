package com.restapi.project_AI_diary_backend.domain.image.repository;

import com.restapi.project_AI_diary_backend.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    // 특정 다이어리의 모든 이미지 반환
    List<Image> findByDiary_DiaryId(Long diaryId);
}

