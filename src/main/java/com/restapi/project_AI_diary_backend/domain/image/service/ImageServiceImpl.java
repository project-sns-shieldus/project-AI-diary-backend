package com.restapi.project_AI_diary_backend.domain.image.service;

import com.restapi.project_AI_diary_backend.domain.diary.entity.Diary;
import com.restapi.project_AI_diary_backend.domain.diary.repository.DiaryRepository;
import com.restapi.project_AI_diary_backend.domain.image.entity.Image;
import com.restapi.project_AI_diary_backend.domain.image.repository.ImageRepository;
import com.restapi.project_AI_diary_backend.domain.image.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final DiaryRepository diaryRepository;

    // 이미지가 저장될 기본 경로를 설정 파일에서 가져옵니다.
    @Value("${image.upload.dir}")
    private String uploadDir;

    public ImageServiceImpl(ImageRepository imageRepository, DiaryRepository diaryRepository) {
        this.imageRepository = imageRepository;
        this.diaryRepository = diaryRepository;
    }

    @Override
    @Transactional
    public void saveImages(Long diaryId, List<MultipartFile> images) throws IOException {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid diary ID: " + diaryId));

        // 업로드 디렉토리 존재 여부 확인 및 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean dirCreated = directory.mkdirs();
            if (!dirCreated) {
                throw new IOException("Failed to create upload directory: " + uploadDir);
            }
        }

        List<Image> imageEntities = new ArrayList<>();

        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                // 요거 사용해서 확장자 필터링 예정. (dall-e가 뭘 뽑아서 주는질 몰라서..)
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                if (!isAllowedExtension(extension)) {
                    throw new IllegalArgumentException("Unsupported file type: " + extension);
                }

                // 중복을 방지용 UUID 변환
                String savedFileName = UUID.randomUUID().toString() + extension;

                // 로컬에 저장
                String filePath = uploadDir + File.separator + savedFileName;
                File dest = new File(filePath);
                file.transferTo(dest);

                // 데이터베이스에 파일명만 저장했다가 컨트롤러단에서 파일명으로 불러서 프론트로 넘겨줄겁니다.
                Image image = Image.builder()
                        .diary(diary)
                        .fileName(savedFileName) // 파일명만 저장
                        .build();

                imageEntities.add(image);
            }
        }

        imageRepository.saveAll(imageEntities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Image> getImagesByDiaryId(Long diaryId) {
        return imageRepository.findByDiary_DiaryId(diaryId);
    }

    @Override
    @Transactional
    public void deleteImagesByDiaryId(Long diaryId) throws IOException {
        List<Image> images = imageRepository.findByDiary_DiaryId(diaryId);

        for (Image image : images) {
            // 로컬에 저장된 파일 삭제
            String filePath = uploadDir + File.separator + image.getFileName();
            File file = new File(filePath);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new IOException("Failed to delete file: " + filePath);
                }
            }

            imageRepository.delete(image);
        }
    }

    // 허용된 파일 확장자 검증 메서드 (임시로 해놓음)
    private boolean isAllowedExtension(String extension) {
        List<String> allowedExtensions = List.of(".jpg", ".jpeg", ".png", ".gif");
        return allowedExtensions.contains(extension.toLowerCase());
    }
}