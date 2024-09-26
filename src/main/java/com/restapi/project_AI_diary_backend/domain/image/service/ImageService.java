package com.restapi.project_AI_diary_backend.domain.image.service;

import com.restapi.project_AI_diary_backend.domain.image.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    // 이미지 저장 메서드
    void saveImages(Long diaryId, List<MultipartFile> images) throws IOException;

    // 다이어리 ID로 이미지 조회 메서드
    List<Image> getImagesByDiaryId(Long diaryId);

    // 이미지 삭제 메서드
    void deleteImagesByDiaryId(Long diaryId) throws IOException;
}